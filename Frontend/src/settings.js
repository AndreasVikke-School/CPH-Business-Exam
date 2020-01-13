const URLs = {
    "Home": "/",
    "Login": "/login",
    "ProductDetails": "/productdetails",
    "NoMatch": "*"
}

function URLSettings() {
    const getURL = (key) => { return URLs[key] }

    return {
        getURL
    }
}
export default URLSettings();


