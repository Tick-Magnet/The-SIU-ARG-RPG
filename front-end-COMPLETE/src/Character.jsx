import APICallContainer from './APICallContainer';
import Popup from './components/Popup';
import {useState} from 'react'; 

export const Character = () => {
    const [playerRace, setPlayerRace] = useState("dwarf");
    const [playerClass, setPlayerClass] = useState("barbarian");
    const [helpPopup, setHelpPopup] = useState(false);

    const temp = (e) => {
        console.log(playerRace);
        console.log(playerClass);
    }

    return (
        <div className="Character">
            <main>

                <h1>Create Your Character:</h1>
                <form className="characterForm" onSubmit={temp}>

                    <label htmlFor='race'>Select your race:</label>
                    <select id='race' name='race' onChange={(e) => setPlayerRace(e.target.value)}>
                        <option value='dwarf'>Dwarf</option>
                        <option value='elf'>Elf</option>
                        <option value='human'>Human</option>
                    </select>

                    <label htmlFor='playerClass'>Select your class:</label>
                    <select id='playerClass' name='playerClass' onChange={(e) => setPlayerClass(e.target.value)}>
                        <option value='barbarian'>Barbarian</option>
                        <option value='knight'>Knight</option>
                        <option value='rogue'>Rogue</option>
                        <option value='wizard'>Wizard</option>
                    </select>

                    <button type='submit'>Create!</button>

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