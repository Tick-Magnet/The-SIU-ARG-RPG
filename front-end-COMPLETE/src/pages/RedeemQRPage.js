import React from 'react'
import {useContext} from 'react';
import {LoginInfoContext} from "../App.js";
import {useState} from "react";
import APICallContainer from "../APICallContainer.js";
import "../components/rpgComponents.css";
import Popup from "../components/Popup";

function RedeemQRPage ()
{
    const params = new URLSearchParams(window.location.search);
    const loginInfo = useContext(LoginInfoContext);

    const [redeemComplete, setRedeemComplete] = useState(false);
    const [redeemResult, setRedeemResult] = useState(null);

    function RedeemContainer()
    {
        if(loginInfo.loggedIn == false)
        {
            return(
                <>
                    <h1>Please login to redeem</h1>
                </>
            );
        }
        else
        {
            return(
            <RedeemResult />
            );
        }
    }

    function EncounterHandler()
    {
        const [currentStep, setCurrentStep] = useState(null);
        const [postStepComplete, setPostStepComplete] = useState(false);
        const [initialStepReceived, setInitialStepReceived] = useState(false);
        const [waitingResponse, setWaitingResponse] = useState(false)

        function InputSelector(props)
        {
            function inputSelectorClick(index)
            {
                console.log(index);
                //Send request with choice
                if(waitingResponse == false)
                {
                    //set a flag to prevent use double clicking a selection
                    setWaitingResponse(true);
                    var update = APICallContainer.postEncounterUpdate(loginInfo.username, loginInfo.sessionToken, index, redeemResult.encounterID).then(
                        function(value)
                        {
                            setCurrentStep(value);
                            setWaitingResponse(false);
                        }
                    );
                }
            }
            return(
                <>
                    <li onClick = {() => inputSelectorClick(props.optionIndex)}>{props.optionText} </li>
                </>
            );
        }
        //Get initial encounter update
        if(initialStepReceived == false){
        var update = APICallContainer.postEncounterUpdate(loginInfo.username, loginInfo.sessionToken, -1, redeemResult.encounterID).then(
            function(value)
                {
                    console.log(value);
                    setCurrentStep(value);
                    setInitialStepReceived(true);
                }
            );
        }
        if(currentStep != null)
        {
            if(currentStep.stepType == 0)
            {
                return(
                    <>
                        <p>dialogue</p>
                        <img className="enemyImage" src={currentStep.backgroundImagePath} />
                        <ul>
                        {currentStep.choices.map((option, index) => <InputSelector optionText={option} optionIndex={index}/>)}
                        </ul>
                    </>
                );
            }
            else if(currentStep.stepType == 1)
            {
                return(
                    <>
                        <p>combat</p>
                        <img className="enemyImage" src={currentStep.enemyImagePath} />
                        <p>Enemy Name: {currentStep.enemyName} </p>
                        <p>Enemy Health: {currentStep.enemyHealth} </p>
                        <p>Player Health: {currentStep.playerHealth} </p>
                        <ul>
                        {currentStep.choices.map((option, index) => <InputSelector optionText={option} optionIndex={index}/>)}
                        </ul>
                    </>
                );
            }
            else if(currentStep.stepType == 3)
            {
                return(
                <>
                    <p>End of encounter Rewards</p>
                    <p>Gold Reward: {currentStep.goldReward} </p>
                    <p>Experience Gained: {currentStep.experienceReward} </p>
                    <p> Items Rewarded: </p>
                    <ul>
                        {currentStep.itemList.map((option, index) => <li> {option} </li>)}
                    </ul>
                </>
                );
            }
        }
    }

    function RedeemResult()
    {
        const [itemPopup, setItemPopup] = useState(false);

        //Make API call
        if(redeemComplete == false)
        {
        var redeem = APICallContainer.redeemQR(loginInfo.username, loginInfo.sessionToken, params.get("uuid")).then(
            function(value)
            {
                console.log(value);
                setRedeemComplete(true);
                setRedeemResult(value);
            }
        );
        }
        if(redeemComplete == false){
            return(
            <div>
                <p> Redeeming QR {params.get("uuid")} </p>

            </div>
            );
        }
        else if(redeemResult.type == 0)
        {
            return(
            <div>
                <input type='image' src='./Images/chest.png' alt='Open Chest' className='chest' onClick={() => setItemPopup(true)}/>
                <Popup trigger={itemPopup} setTrigger={setItemPopup}>
                <p>Gold Received: {redeemResult.gold}</p>
                <p>Experience Received: {redeemResult.experience}</p>
                <p>Item Received: {redeemResult.item.name}</p>
                <img className="enemyImage" src={redeemResult.imagePath} />
                </Popup>
            </div>
            );
        }
        else if(redeemResult.type == 1)
        {
            return(
            <div>
                <p>encounter</p>
                <EncounterHandler />
            </div>
            );
        }

    }

    return(
        <>
            <RedeemContainer />
        </>
    );
}

export default RedeemQRPage;