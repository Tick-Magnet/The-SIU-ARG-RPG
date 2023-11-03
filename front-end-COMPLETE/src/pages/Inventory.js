import React from 'react'
import {useContext} from 'react';
import {LoginInfoContext} from "../App.js";
import {useState} from "react";
import APICallContainer from "../APICallContainer.js";
import "../components/rpgComponents.css";



function Inventory ()
{
const [waitingInventory, setWaitingInventory] = useState(true);
const [currentInventory, setCurrentInventory] = useState(null);
const loginInfo = useContext(LoginInfoContext);

    function InventoryItem(props)
    {
        const[waitingItem, setWaitingItem] = useState(true);
        const [currentItem, setCurrentItem] = useState(false);
        const [displayCurrentItem, setDisplayCurrentItem] = useState(false);

        function ItemDetails()
        {

            if(displayCurrentItem == true)
            {
                return(
                    <>
                        <p>displaying</p>
                    </>
                );
            }
            else
            {
                return (null);
            }
        }

        return (
            <>
                <p className="inventoryItem" onClick={() => setDisplayCurrentItem(!displayCurrentItem)}>{props.item}</p>
                <ItemDetails />
            </>
        );
    }


    if(loginInfo.loggedIn == true)
    {
        if(currentInventory == null)
        {
            var inventoryResult = APICallContainer.getInventory(loginInfo.username, loginInfo.sessionToken).then(
            function(value)
            {
                setCurrentInventory(value);
                setWaitingInventory(false);
            }
            );
        }
        if(waitingInventory == true)
        {
            return(
                <>
                    <p>Loading inventory </p>
                </>
            );
        }
        else
        {
            return(
                <>
                     <ul>
                     {currentInventory.inventory.map((option, index) => <InventoryItem item={option} />)}
                     </ul>
                </>
            );
        }
    }
    else
    {
        return(
            <p> Please login to view inventory </p>
        );
    }
}


export default Inventory;