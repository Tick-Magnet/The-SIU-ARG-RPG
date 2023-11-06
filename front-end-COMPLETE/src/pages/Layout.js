import React from 'react'
import Navbar from '../components/Navbar.js';
import {Outlet} from "react-router-dom";
import Footer from '../components/Footer';

export default function Layout()
{
    return(
    <>
        <Navbar />
        <Outlet />
        <Footer /> {/* Add the Footer component here */}
    </>
    );
}

