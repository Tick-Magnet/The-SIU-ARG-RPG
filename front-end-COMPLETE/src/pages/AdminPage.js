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
    const [currentCommand, setCurrentCommand] = useState(null)
    const [consoleText, setConsoleText] = useState(null);
    function QRCreator()
    {


    }

    function sendCommand(e)
    {
        e.preventDefault();
        var data = new FormData(e.target);
        var entries = Object.fromEntries(data.entries());
        console.log(entries.command);
        if(entries.command != null && entries.command != "")
        {
            if(consoleText == null)
            {
                setConsoleText(">" + entries.command);
            }
            else
            {
                setConsoleText(consoleText + "\n>" + entries.command);
            }

            if(entries.command == "clear")
            {
                setConsoleText(null);
            }
            else
            {
                //attempt to run command through API
                var result = APICallContainer.shellCommand(loginInfo.username, loginInfo.sessionToken, entries.command).then(
                    function(value)
                    {
                    console.log(value);
                        setConsoleText(consoleText + "\n" + value.result);
                    }
                );
            }
        }
    }

    function CommandConsole()
    {
        return(
            <>
                <textarea value={consoleText} readOnly={true} rows={20} cols={90} className="commandConsole">

                </textarea>
                <form className="login-form" onSubmit={sendCommand}>
                    <input name="command" type="text"  />

              </form>
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