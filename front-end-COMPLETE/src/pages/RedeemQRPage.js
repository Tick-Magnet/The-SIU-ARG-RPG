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
                <div className="main">
                    <h1>Please login to redeem</h1>
                </div>
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

                        <div className="main">
                            <p>{currentStep.promptText}</p>
                            <img className="enemyImage" src={currentStep.backgroundImagePath} />
                            <ul className="optionsList">
                            {currentStep.choices.map((option, index) => <InputSelector optionText={option} optionIndex={index}/>)}
                            </ul>
                        </div>
                        </>
                    );
                }
                else if(currentStep.stepType == 1)
                {
                    return(
                        <>
                        <div className="main">
                            <p id="enemyStats">
                                <span>Enemy: {currentStep.enemyName} </span>
                                <span>Enemy Health: {currentStep.enemyHealth}</span>
                            </p>
                            <img className="enemyImage" src={currentStep.enemyImagePath} />
                            <p>Player Health: {currentStep.playerHealth} </p>
                            <ul className="optionsList">
                            {currentStep.choices.map((option, index) => <InputSelector optionText={option} optionIndex={index}/>)}
                            </ul>
                        </div>
                        </>
                    );
                }
                else if(currentStep.stepType == 3)
                {
                    return(

                    <>
                    <div className="main" id="rewards">
                        <h3 id="title">Loot!</h3>
                        <p>Gold: {currentStep.goldReward} </p>
                        <p>Experience: {currentStep.experienceReward} </p>
                        
                        {/* Code for if Items are implemented.
                        <p style={{textDecoration:"underline"}}> Items: </p>
                        <ul>
                            {currentStep.itemList.map((option, index) => <li> {option} </li>)}
                        </ul>
                        */}
                    </div>
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
                <p>Gold Received: {redeemResult.goldReward}</p>
                <p>Experience Received: {redeemResult.experienceReward}</p>
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
                <EncounterHandler />
            </div>
            );
        }
        else if(redeemResult.type == 2)
        {
            return (
              <>
                <img src={redeemResult.backgroundImagePath} />
                <p style={{
                  color: 'white',
                  fontFamily: 'Papyrus, fantasy',
                  textAlign: 'center'
                }}>
                  You have been fully healed
                </p>
              </>
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
