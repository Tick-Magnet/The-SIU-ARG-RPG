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

import {createContext, useContext} from 'react';
export const LoginInfoContext = createContext({username: null, sessionToken: null});
function App()
{
    const [username, setUsername] = useState(null);
    const [sessionToken, setSessionToken] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);



    useEffect(() => {
        


    });

    return(
    <LoginInfoContext.Provider value={{username: username, sessionToken: sessionToken}}>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<HomePage />} />
                    <Route path="redeemQR" element={<RedeemQRPage />} />

                </Route>
            </Routes>
        </BrowserRouter>
    </LoginInfoContext.Provider>
    )
}

export default App;
