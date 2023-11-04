import React from 'react'
import {useContext} from 'react';
import {LoginInfoContext} from "../App.js";
import {useState} from "react";
import APICallContainer from "../APICallContainer.js";
import "../components/rpgComponents.css";


import Popup from '../components/Popup';


function CreateCharacter() {
    const [character, setCharacter] = useState(null);
    const [statsRolled, setStatsRolled] = useState(false);
    const [requestSent, setRequestSent] = useState(false);
    const [playerRace, setPlayerRace] = useState("dwarf");
    const [playerClass, setPlayerClass] = useState("barbarian");
    const [helpPopup, setHelpPopup] = useState(false);
    const loginInfo = useContext(LoginInfoContext);

    function handleSubmit(e)
    {
        e.preventDefault();
        setRequestSent(true);
        var result = APICallContainer.createCharacter(loginInfo.username, loginInfo.sessionToken).then(
            function(value)
            {
                var result2 = APICallContainer.createCharacter(loginInfo.username, loginInfo.sessionToken, playerClass, playerRace).then(
                    function(value)
                    {
                        setCharacter(value);
                        if(value.creationComplete == true)
                        {
                            loginInfo.setCharacterCreated(true);
                        }
                    }
                );
            }
        );
    }
    if(loginInfo.loggedIn == true && requestSent == false && loginInfo.characterCreated == false){
    return (
        <div className="Character">
            <main>

                <h1>Create Your Character:</h1>
                <form className="characterForm" onSubmit={handleSubmit}>

                    <label htmlFor='race'>Select your race:</label>
                    <select id='race' name='race' onChange={(e) => setPlayerRace(e.target.value)}>
                        <option value='DWARF'>Dwarf</option>
                        <option value='ELF'>Elf</option>
                        <option value='HUMAN'>Human</option>
                    </select>

                    <label htmlFor='playerClass'>Select your class:</label>
                    <select id='playerClass' name='playerClass' onChange={(e) => setPlayerClass(e.target.value)}>
                        <option value='BARBARIAN'>Barbarian</option>
                        <option value='KNIGHT'>Knight</option>
                        <option value='ROGUE'>Rogue</option>
                        <option value='WIZARD'>Wizard</option>
                    </select>

                    <button type="submit">Create!</button>

                </form>

                <input type='image' src='./Images/help.png' alt='help' className='helpButt' onClick={() => setHelpPopup(true)}/>

                <Popup trigger={helpPopup} setTrigger={setHelpPopup}>
                    <h3>Race Details</h3>
                    <div className='details'>
                        <ul>
                            <li>Dwarf</li>
                            <ul>
                                <li>+1 Con</li>
                                <li>+1 Str</li>
                                <li>-1 Dex</li>
                            </ul>
                            <li>Elf</li>
                            <ul>
                                <li>+1 Dex</li>
                                <li>+1 Int</li>
                                <li>-1 Str</li>
                            </ul>
                            <li>Human</li>
                            <ul>
                                <li>No modifier</li>
                            </ul>
                        </ul>
                    </div>

                    <h3>Class Details</h3>
                    <div className='details'>
                        <ul>
                            <li>Barbarian</li>
                            <ul>
                                <li>Strong stat: Str</li>
                                <li>Weak stat: Int</li>
                            </ul>
                            <li>Knight</li>
                            <ul>
                                <li>Strong stat: Con</li>
                                <li>Weak stat: Dex</li>
                            </ul>
                            <li>Rogue</li>
                            <ul>
                                <li>Strong stat: Dex</li>
                                <li>Weak stat: Str</li>
                            </ul>
                            <li>Wizard</li>
                            <ul>
                                <li>Strong stat: Int</li>
                                <li>Weak stat: Con</li>
                            </ul>
                        </ul>
                    </div>
                </Popup>
            </main>
        </div>
      );
      }
      else if(requestSent == true)
      {
          if(character == null)
          {
                return(
                    <p> Creating character, please wait</p>
                );
          }
          else
          {
                return(
                <>
                    <h1>Character Created! You are ready to play!</h1>
                    <h2>What next?</h2>
                    <p>QR codes can be found around campus to unlock items or start combat encounters. Scan them with your
                    devices camera app and follow the link provided. Locations of QR codes can be displayed by clicking on the
                    Map button on the navigation bar above</p>
                </>
                );
          }
      }
      else if(loginInfo.loggedIn == false)
      {
        return(
            <p>Please login before creating your character</p>
        );
      }
      else if(loginInfo.characterCreated == true)
      {
            return(
                <>
                    <h1>Character Created! You are ready to play!</h1>
                    <h2>What next?</h2>
                    <p>QR codes can be found around campus to unlock items or start combat encounters. Scan them with your
                    devices camera app and follow the link provided. Locations of QR codes can be displayed by clicking on the
                    Map button on the navigation bar above</p>
                </>
                );
      }
}

export default CreateCharacter;