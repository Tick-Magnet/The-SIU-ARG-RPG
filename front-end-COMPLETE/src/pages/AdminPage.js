import React from 'react'
import {useContext} from 'react';
import {LoginInfoContext} from "../App.js";
import {useState} from "react";
import APICallContainer from "../APICallContainer.js";
import "../components/rpgComponents.css";


import Popup from '../components/Popup';

function AdminPage()
{
    const loginInfo = useContext(LoginInfoContext);

    function QRCreator()
    {


    }

    function CommandConsole()
    {
        return(
            <>
                <textarea readOnly="true" className="commandConsole">

                </textarea>
            </>
        );
    }

    if(loginInfo.userRole == "ADMIN")
    {
        return(
            <>
            <h2>Admin Dashboard</h2>
            <QRCreator />
            <CommandConsole />
            </>
        );
    }
    else
    {
        return(
        <>
            <p>Unauthorized</p>
        </>
        );
    }
}

export default AdminPage;