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
const [character, setCharacter] = useState(null);
const [characterRequested, setCharacterRequested] = useState(false);
const loginInfo = useContext(LoginInfoContext);

    function CharacterInfo()
    {
        if(character != null)
        {
            return(
                <>
                    <p>Level: {character.level}</p>
                    <p>Gold: {character.gold}</p>
                    <p>Health: {character.health}</p>
                    <p>Experience: {character.experience} </p>
                    <p>Strength: {character.strength} </p>
                    <p>Dexterity: {character.dexterity} </p>
                    <p>Constitution: {character.constitution}</p>
                    <p>Intelligence: {character.intelligence}</p>

                </>
            );
        }
    }
    function InventoryItem(props)
    {
        const[waitingItem, setWaitingItem] = useState(true);
        const [currentItem, setCurrentItem] = useState(null);
        const [displayCurrentItem, setDisplayCurrentItem] = useState(false);


        function ItemDetails()
        {

            if(displayCurrentItem == true)
            {
                if(currentItem != null)
                {
                    return(
                        <>
                            <p>Description: {currentItem.item.itemDescription} </p>
                            <p>Item Type: {currentItem.item.itemType}</p>
                            <p>Item Grade: {currentItem.item.itemGrade}</p>
                            <p>Gold Value: {currentItem.item.goldValue}</p>

                        </>
                    );
                }
                else
                {
                    var itemResult = APICallContainer.inspectItemSlot(loginInfo.username, loginInfo.sessionToken, props.index).then(
                        function(value)
                        {
                            setCurrentItem(value);
                        }
                    );
                }
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
        if(characterRequested == false)
        {
            var result = APICallContainer.getCharacter(loginInfo.username, loginInfo.sessionToken).then(
                function(value)
                {
                    console.log(value);
                    setCharacterRequested(true);
                    setCharacter(value);
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
                    <h2>Character</h2>
                        <CharacterInfo />
                    <h2>Items</h2>
                     <ul>
                     {currentInventory.inventory.map((option, index) => <InventoryItem item={option} index={index} />)}
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