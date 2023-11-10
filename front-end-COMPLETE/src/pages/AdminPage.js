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
    const [currentQRCode, setCurrentQRCode] = useState(null);

    function getQRCode(e)
    {
        e.preventDefault();
        var data = new FormData(e.target);
        var entries = Object.fromEntries(data.entries());
        console.log(entries);
        var result = APICallContainer.createQRCode(loginInfo.username, loginInfo.sessionToken, parseInt(entries.type), parseInt(entries.colorType), entries.itemDefinitionPath, entries.encounterDefinitionPath, parseInt(entries.experienceReward), parseInt(entries.goldReward), entries.backgroundImagePath).then(
            function(value)
            {
                console.log(value);
                setCurrentQRCode(value.image);
            }
        );
    }

    function QRCreator()
    {
        return(
            <>
                <h3>QR Code Generator</h3>
                <form className="login-form" onSubmit={getQRCode}>
                    <label htmlFor="type">Type: </label>
                    <input name="type" type="number"  />
                    <label> Item Definition Path </label>
                    <input name="itemDefinitionPath" type="text" />
                    <label> Encounter Definition Path </label>
                    <input name="encounterDefinitionPath" type="text" />
                    <label> Gold Reward </label>
                    <input name="goldReward" type="number"/>
                    <label> Experience Reward: </label>
                    <input name="experienceReward" type="number" />
                    <label> Color Type </label>
                    <input name="colorType" type="number" />
                    <label> Background Image Path (healing only) </label>
                    <input name="backgroundImagePath" type="text" />
                    <button type="submit">Generate</button>

                </form>
                <img  className="qrImage"  src = {"data:image/jpg;base64," + currentQRCode} />
            </>
        );
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
                <h3> Command Console </h3>
                <p>
                    run showCommands to list methods available. View the ShellCommands class in the backend
                    code for information on available methods or to add methods.
                </p>
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
            <div className="adminContainer">
            <main>
            <h2>Admin Dashboard</h2>
            <QRCreator />
            <CommandConsole />
            </main>
            </div>
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