import React from 'react'
import './rpgComponents.css'

function test()
{
    console.log("test");
}

function Navbar() {
    return(
        <ul className="rpgNavbar">
            <li className="navItem"><a href="/"> Home </a> </li>
            <li className="navItem"><a href="/Inventory"> Inventory </a> </li>
            <li className="navItem"><a href="/Map"> Map </a> </li>
            <li className="navItem"><a href="/Contact"> Contact Information </a> </li>
            <li className="loginButton" onClick = {() => test()}><a>Login</a></li>

        </ul>
    );
}

export default Navbar