const URLs = {
    "Home": "/",
    "Login": "/login",
    "Data": "/data",
    "NoMatch": "*"
}

function URLSettings() {
    const getURL = (key) => { return URLs[key] }

    return {
        getURL
    }
}
export default URLSettings();


