const URLs = {
    "Home": "/",
    "Login": "/login",
    "ProductDetails": "/productdetails",
    "NoMatch": "*"
}

function URLSettings() {
    const getURL = (key, param = null) => { return URLs[key] + (param !== null ? "/:" + param : "") }

    return {
        getURL
    }
}
export default URLSettings();


