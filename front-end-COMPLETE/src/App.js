import React, {useState} from 'react';
import { Item } from './Item';
import { Combat } from './Combat';
import Popup from './components/Popup';
import Cookies from 'universal-cookie';

import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "./pages/HomePage";
import RedeemQRPage from "./pages/RedeemQRPage";

function App()
{
    return(
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<HomePage/>}></Route>
                <Route exact path="/redeemQR" element={<RedeemQRPage/>}></Route>
            </Routes>
        </BrowserRouter>

    )
}

export default App;
