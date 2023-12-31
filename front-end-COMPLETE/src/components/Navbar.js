import React from 'react'
import './rpgComponents.css'
import {Outlet, Link} from "react-router-dom";
import {useState} from 'react';
import Popup from './Popup.js'
import APICallContainer from "../APICallContainer.js"
import {useContext} from 'react';
import {LoginInfoContext} from "../App.js";


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

  const loginInfo = useContext(LoginInfoContext);
  const [username, setUsername] = useState(null);
  const [sessionToken, setSessionToken] = useState(null);
  const [loggedIn, setLoggedIn] = useState(false);

  const [loginError, setLoginError] = useState(null);
  const [registerError, setRegisterError] = useState(null);
  const [registerButtonVisible, setRegisterButtonVisible] = useState(true);

  function RegisterButton()
  {
    if(registerButtonVisible)
    {
        return(
          <>
             <br></br><button type="submit">Register</button>
          </>
        );
    }
  }

   const handleLoginSubmit = (e) => {
      e.preventDefault();
      console.log(name);
      //console.log(email);
      console.log(pass);

      var response = APICallContainer.login(email, pass);
      response.then(
        function(value)
        {
            console.log(value.username + "VALUE");
            if(value.loggedIn == true)
            {

                loginInfo.setUsername(value.username);
                loginInfo.setSessionToken(value.sessionToken);
                loginInfo.setLoggedIn(true);
                loginInfo.setUserRole(value.userRole);
                loginInfo.setCharacterCreated(value.characterCreated);

                setLoginPopup(false);
            }
            else
            {
                setLoginError(value.message);
                console.log(value.message);
            }
        }
      );
    }

    const handleRegisterSubmit = (e) => {
        e.preventDefault();
        setRegisterButtonVisible(false);
        var response = APICallContainer.register(name, email, pass).then(
            function(value)
            {
                if(value.registered == true)
                {
                    setRegisterPopup(false);
                }
                else
                {
                    setRegisterError(value.status);
                    setRegisterButtonVisible(true);
                }
            }
        );
    }

    function toRegister() {
      setRegisterPopup(true);
      setLoginPopup(false);
    }

    function toLogin() {
      setLoginPopup(true);
      setRegisterPopup(false);
    }

    function logout()
    {
        console.log("logging out");
        loginInfo.setUsername(null);
        loginInfo.setSessionToken(null);
        loginInfo.setLoggedIn(false);
        setRegisterButtonVisible(true);
        APICallContainer.logout();
    }

    function LoginButton(){

        if(loginInfo.loggedIn == true)
        {
            return (
            <li className="loginButton" onClick = {() => logout()}><a>Hello {loginInfo.username} Click here to logout</a></li>
            );
        }
        else
        {
            return (
                <li className="loginButton" onClick = {() => toLogin()}><a>Login</a></li>
            );
        }

    }

    function InventoryButton()
    {

     if(loginInfo.loggedIn == true)
            {
                return (
                <li className="navItem"><Link to="/Inventory"> Inventory </Link> </li>
                );
            }
            else
            {
                return (
                    null
                );
            }

    }
    function AdminDashboard()
    {
        if(loginInfo.userRole != null && loginInfo.userRole == "ADMIN")
        {
            console.log(loginInfo.userRole);
            return(
                <li className="navItem"><Link to="/Admin"> Admin Dashboard </Link> </li>
            );
            }
    }
    function CharacterButton()
    {
        console.log(loginInfo.characterCreated);
        console.log(loginInfo);
        if(loginInfo.loggedIn == true && loginInfo.characterCreated != true)
        {
            return(
                <>
                    <li className="navItem"><Link to="/CreateCharacter"> Create Character </Link> </li>
                </>
            );
        }
        else
        {
            return null;
        }
    }
    return(

    <>

        <ul className="rpgNavbar">
            <li className="navItem"><Link to="/"> Home </Link> </li>
            <CharacterButton />
            <InventoryButton />
            <AdminDashboard />
            <li className="navItem" onClick = {() => setMapPopup(true)}><a> Map </a> </li>

            <LoginButton />

        </ul>
    <Popup trigger={mapPopup} setTrigger={setMapPopup}>
            <br></br><br></br>
            <div className='map'>
                <img src='./Images/ARGMap.png' alt='Map of QR codes' />
            </div>
            
          </Popup>

          <Popup trigger={loginPopup} setTrigger={setLoginPopup}>
            <div className='auth-form-container'>
              <h1><center>Log In</center></h1>
              <form className="login-form" onSubmit={handleLoginSubmit}>
                <label htmlFor="email">Username: </label>
                <input value={email} onChange={(e) => setEmail(e.target.value)} type="text" placeholder="Xx_NoobSlayer435_xX" id="email" name="email" />
                <label htmlFor="password">Password: </label>
                <input value={pass} onChange={(e) => setPass(e.target.value)} type="password" placeholder="********" id="password" name="password" />
                <div className='logIn-btn'>
                    <br></br><button type="submit">Log In</button>
                </div>
                <p>{loginError}</p>
                <button onClick={() => toRegister()} className='link-button'>Don't have an account? Register here.</button>
              </form>
            </div>
          </Popup>

          <Popup trigger={registerPopup} setTrigger={setRegisterPopup}>
            <h1>Register</h1>
            <div className='auth-form-container'>
                <p>{registerError}</p>
              <form className="register-form" onSubmit={handleRegisterSubmit}>
                <label htmlFor="name">Username: </label>
                <input onChange={(e) => setName(e.target.value)} placeholder="Xx_NoobSlayer435_xX" id="name" name="name" />
                <label htmlFor="email">Email: </label>
                <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Placeholder@email.com" id="email" name="email" />
                <label htmlFor="password">Password: </label>
                <input value={pass} onChange={(e) => setPass(e.target.value)} type="password" placeholder="********" id="password" name="password" />
                <RegisterButton />
                <br></br><button onClick={() => toLogin()} className='link-button'>Already have an account? Login here.</button>

                <p> After registering, check your email for a verification link. The email may appear in your junk mail folder </p>
              </form>
            </div>
          </Popup>
      </>
    );
}

export default Navbar