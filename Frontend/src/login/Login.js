import React, { useState} from 'react';

export default function Login(props) {
    const [state, setState] = useState();

    const onSubmit = (evt) => {
        evt.preventDefault();
        if(evt.target.id !== "register")
            props.login(state.username, state.password);
        else
            props.register(state.username, state.password, state.password2);
    }

    const onChange = (evt) => {
        setState({ ...state, [evt.target.id]: evt.target.value });
    }

    return (
        <div className="container">
            <h1>Login</h1>
            <form id="login" onSubmit={onSubmit} onChange={onChange}>
                <div className="form-group">
                    <input type="text" className="form-control" placeholder="Username" id="username" />
                </div>
                <div className="form-group">
                    <input type="password" className="form-control" placeholder="Password" id="password" />
                </div>
                <input type="submit" value="Login" className="btn btn-primary"/>
            </form>
            <br/><hr/><br/>
            <h1>Register</h1>
            <form id="register" onSubmit={onSubmit} onChange={onChange}>
                <div className="form-group">
                    <input type="text" className="form-control" placeholder="Username" id="username" />
                </div>
                <div className="form-group">
                    <input type="password" className="form-control" placeholder="Password" id="password" />
                </div>
                <div className="form-group">
                    <input type="password" className="form-control" placeholder="Password" id="password2" />
                </div>
                <input type="submit" value="Register" className="btn btn-primary"/>
            </form>
        </div>
    )
}