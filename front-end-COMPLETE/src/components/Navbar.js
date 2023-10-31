import React from 'react'
import './rpgComponents.css'
import {Outlet, Link} from "react-router-dom";
import {useState} from 'react';
import Popup from './Popup.js'

function test()
{
    console.log("test");
}

function Navbar() {
  const [loginPopup, setLoginPopup] = useState(false);
  const [registerPopup, setRegisterPopup] = useState(false);
  const [mapPopup, setMapPopup] = useState(false);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [pass, setPass] = useState('');



   const handleSubmit = (e) => {
      e.preventDefault();
      console.log(name);
      console.log(email);
      console.log(pass);
    }

    function toRegister() {
      setRegisterPopup(true);
      setLoginPopup(false);
    }

    function toLogin() {
      setLoginPopup(true);
      setRegisterPopup(false);
    }

    return(
    <>
        <ul className="rpgNavbar">
            <li className="navItem"><Link to="/"> Home </Link> </li>
            <li className="navItem"><Link to="/Inventory"> Inventory </Link> </li>
            <li className="navItem"><Link to="/Map"> Map </Link> </li>
            <li className="navItem"><Link to="/Contact"> Contact Information </Link> </li>
            <li className="loginButton" onClick = {() => toLogin()}><a>Login</a></li>

        </ul>
    <Popup trigger={mapPopup} setTrigger={setMapPopup}>
            <img src='./Images/Map.png' alt='Map of QR codes' />
          </Popup>

          <Popup trigger={loginPopup} setTrigger={setLoginPopup}>
            <div className='auth-form-container'>
              <h1>Log In</h1>
              <form className="login-form" onSubmit={handleSubmit}>
                <label htmlFor="email">Email: </label>
                <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Placeholder@email.com" id="email" name="email" />
                <label htmlFor="password">Password: </label>
                <input value={pass} onChange={(e) => setPass(e.target.value)} type="password" placeholder="********" id="password" name="password" />
                <button type="submit">Log In</button>
                <button onClick={() => toRegister()} className='link-button'>Don't have an account? Register here.</button>
              </form>
            </div>
          </Popup>

          <Popup trigger={registerPopup} setTrigger={setRegisterPopup}>
            <h1>Register</h1>
            <div className='auth-form-container'>
              <form className="register-form" onSubmit={handleSubmit}>
                <label htmlFor="name">Full Name: </label>
                <input onChange={(e) => setName(e.target.value)} placeholder="John Doe" id="name" name="name" />
                <label htmlFor="email">Email: </label>
                <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Placeholder@email.com" id="email" name="email" />
                <label htmlFor="password">Password: </label>
                <input value={pass} onChange={(e) => setPass(e.target.value)} type="password" placeholder="********" id="password" name="password" />
                <button type="submit">Register</button>
                <button onClick={() => toLogin()} className='link-button'>Already have an account? Login here.</button>
              </form>
            </div>
          </Popup>
      </>
    );
}

export default Navbar