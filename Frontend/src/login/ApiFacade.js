const URL = "http://localhost:8080/exam"; // Local
//const URL = "https://andreasvikke.dk/Exam"; // Deployed

function handleHttpErrors(res) {
    if (!res.ok) {
        return Promise.reject({ status: res.status, fullError: res.json() })
    }
    return res.json();
}

function ApiFacade() {
    //Insert utility-methods from a latter step (d) here
    const setToken = (token) => {
        localStorage.setItem('jwtToken', token)
    }
    const getToken = () => {
        return localStorage.getItem('jwtToken')
    }
    const loggedIn = () => {
        const loggedIn = getToken() != null;
        return loggedIn;
    }
    const checkLoggedIn = () => {
        return fetchUser();
    }
    const logout = () => {
        localStorage.removeItem("jwtToken");
    }
    
    const makeOptions = (method, addToken, body) => {
        var opts = {
            method: method,
            headers: {
                "Content-type": "application/json",
                'Accept': 'application/json',
            }
        }
        if (addToken && loggedIn()) {
            opts.headers["x-access-token"] = getToken();
        }
        if (body) {
            opts.body = JSON.stringify(body);
        }
        return opts;
    }

    const login = (user, pass) => {
        const options = makeOptions("POST", true, { username: user, password: pass });
        return fetch(URL + "/api/login", options)
            .then(handleHttpErrors)
            .then(res => { setToken(res.token) })
    }

    const register = (user, pass) => {
        const options = makeOptions("POST", true, { username: user, password: pass });
        return fetch(URL + "/api/login/register", options)
            .then(handleHttpErrors)
            .then(res => { setToken(res.token) })
    }

    const fetchUser = () => {
        const options = makeOptions("GET", true); //True add's the token
        return fetch(URL + "/api/info/user", options).then(handleHttpErrors);
    }

    const fetchAllRecipies = () => {
        return fetch(URL + "/api/recipe/all", makeOptions("GET")).then(handleHttpErrors);
    }

    const fetchRecipeByName = (name) => {
        return fetch(URL + "/api/recipe/findByName/" + name, makeOptions("GET")).then(handleHttpErrors);
    }

    const fetchMenuPlanByWeek = (week, username) => {
        return fetch(URL + "/api/menuplan/week/" + week + "?username=" + username, makeOptions("GET", true)).then(handleHttpErrors);
    }

    const createMenuPlan = (menuPlan) => {
        return fetch(URL + "/api/menuplan/add", makeOptions("POST", true, menuPlan)).then(handleHttpErrors);
    }

    const editMenuPlan = (menuPlan) => {
        return fetch(URL + "/api/menuplan/edit", makeOptions("POST", true, menuPlan)).then(handleHttpErrors);
    }

    const deleteMenuPlan = (id) => {
        return fetch(URL + "/api/menuplan/delete/" + id, makeOptions("DELETE", true)).then(handleHttpErrors);
    }

    return {
        login,
        register,
        checkLoggedIn,
        logout,
        fetchUser,
        fetchAllRecipies,
        fetchMenuPlanByWeek,
        createMenuPlan,
        editMenuPlan,
        fetchRecipeByName,
        deleteMenuPlan
    }

}

export default ApiFacade();