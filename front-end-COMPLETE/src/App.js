import React, {useState, useEffect} from 'react';
import { Item } from './Item';
import { Combat } from './Combat';
import Popup from './components/Popup';
import Cookies from 'universal-cookie';

import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "./pages/HomePage";
import RedeemQRPage from "./pages/RedeemQRPage";
import Layout from "./pages/Layout.js";
import APICallContainer from "./APICallContainer.js"
import Inventory from "./pages/Inventory.js";
import CreateCharacter from "./pages/CharacterCreation.js"
import AdminPage from "./pages/AdminPage.js";

import {createContext, useContext} from 'react';
export const LoginInfoContext = createContext({username: null, sessionToken: null, loggedIn: false, userRole: null, characterCreated: false, setUsername: () => {}, setSessionToken: () => {}, setLoggedIn: () => {}, setUserRole: () => {}, setCharacterCreated: () => {}});
function App()
{
    const [username, setUsername] = useState(null);
    const [sessionToken, setSessionToken] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userRole, setUserRole] = useState(null);
    const [characterCreated, setCharacterCreated] = useState(false);


    useEffect(() => {
        var loginInfo = APICallContainer.getLoginInfo();
        loginInfo.then(
           function(value)
           {
           //console.log(value);
                setUsername(value.username);
                setSessionToken(value.sessionToken);
                setIsLoggedIn(value.loggedIn);
                setCharacterCreated(value.characterCreated);
                console.log(value.characterCreated);
                setUserRole(value.userRole);
                console.log(value.userRole);

           }
        );

    }, []);

    return(
    <LoginInfoContext.Provider value={{username: username, sessionToken: sessionToken, loggedIn: isLoggedIn, userRole: userRole, characterCreated: characterCreated, setCharacterCreated: setCharacterCreated, setUserRole: setUserRole, setUsername: setUsername, setSessionToken: setSessionToken, setLoggedIn: setIsLoggedIn}}>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<HomePage />} />
                    <Route path="redeemQR" element={<RedeemQRPage />} />
                    <Route path="Inventory" element={<Inventory />} />
                    <Route path="CreateCharacter" element={<CreateCharacter />} />
                    <Route path="Admin" element={<AdminPage />} />
                </Route>
            </Routes>
        </BrowserRouter>
    </LoginInfoContext.Provider>
    )
}

export default App;
