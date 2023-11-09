import Popup from './components/Popup';
import APICallContainer from './APICallContainer';
import {useState} from 'react';

export const Combat = (props) => {
  const [loginPopup, setLoginPopup] = useState(false);
  const [registerPopup, setRegisterPopup] = useState(false);
  const [mapPopup, setMapPopup] = useState(false);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [pass, setPass] = useState('');

  const [imagePath, setImagePath] = useState("");
  const [health, setHealth] = useState("");
  const [enemyName, setEnemyName] = useState("");
  const [enemyHealth, setEnemyHealth] = useState();
  const [playerHealth, setPlayerHealth] = useState();

  const[promptText, setPromptText] = useState("");
  const[stepType, setStepType] = useState(100);
  const[dialogueOptions, setDialogueOptions] = useState([]);
  const[backgroundImage, setBackgroundImage] = useState();

  const[encounterID, setEncounterID] = useState("1f243eda-4120-4fe1-a28d-951c8bbbbc77");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(name);
    console.log(email);
    console.log(pass);
  }

  function toRegister() {
    setRegisterPopup(true);
    setLoginPopup(false);
  }

  function toLogin() {
    setLoginPopup(true);
    setRegisterPopup(false);
  }
  function postUpdate(selectedChoice)
  {
    var callOutput = APICallContainer.postEncounterUpdate(selectedChoice, encounterID);

    if(callOutput.stepType == 0)
    {
        setStepType(0);
        setPromptText(callOutput.promptText);
        setDialogueOptions(callOutput.choices);
        setBackgroundImage(callOutput.backgroundImagePath);
    }
    else if(callOutput.stepType == 1)
    {
        setStepType(1);
        setBackgroundImage(callOutput.backgroundImagePath);
        setEnemyName(callOutput.enemyName);
        setImagePath(callOutput.enemyImagePath);
        setEnemyHealth(callOutput.enemyHealth);
        setPlayerHealth(callOutput.playerHealth);
    }
  }
  function EnemyDisplay()
  {


    return(
        <div>
            <div>
                <img src={imagePath}></img>
            </div>
            <div>
                <p>Name: {enemyName}</p>
                <p>Enemy Health: {enemyHealth} </p>
                <p>Player Health: {playerHealth} </p>
            </div>
        </div>
    );
  }

  function DialogueDisplay()
  {


    return(
        <div>
            <p>{promptText} </p>
        </div>
    );
  }
  function DialogueOption(props)
  {

      return (
      <div>
        <button onClick={() => postUpdate(props.index)}> </button>
      </div>
      );
  }
  function PlayerInput()
  {
    return(
        <div>
            <ul>
                {dialogueOptions.map((option, index) => <DialogueOption optionText={option.text} optionIndex={index}/>)}
            </ul>
        </div>
    )
  }
  function EncounterWrapper()
  {

    if(stepType == 0)
    {
        return (
        <div style={{backgroundColor: "red"}}>
            <EnemyDisplay />
            <PlayerInput />
        </div>
        );
    }
    else if (stepType == 1)
    {

    }
    else if (stepType == 100)
    {
        postUpdate(-1);
    }
  }
  return (
    <div className="Combat">
        <input type='image' src='./Images/compass.png' alt='Map' className='compass' onClick={() => setMapPopup(true)}/>
        <input type='image' src='./Images/profile.jfif' alt='Login' className='loginbutt' onClick={() => setLoginPopup(true)}/>
      <main>
        <EncounterWrapper />
      </main>

      <Popup trigger={mapPopup} setTrigger={setMapPopup}>
        <img src='./Images/Map.png' alt='Map of QR codes' />
      </Popup>

      <Popup trigger={loginPopup} setTrigger={setLoginPopup}>
        <div className='auth-form-container'>
          <h1>Log In?</h1>
          <form className="login-form" onSubmit={handleSubmit}>            
            <label htmlFor="email">Email: </label>
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Placeholder@email.com" id="email" name="email" />
            <label htmlFor="password">Password: </label>
            <input value={pass} onChange={(e) => setPass(e.target.value)} type="password" placeholder="********" id="password" name="password" />
            <button type="submit">Log In</button>
            <button onClick={() => toRegister()} className='link-button'>Don't have an account? Register here.</button>
          </form>
        </div>
      </Popup>

      <Popup trigger={registerPopup} setTrigger={setRegisterPopup}>
        <h1>Register</h1>
        <div className='auth-form-container'>
          <form className="register-form" onSubmit={handleSubmit}>    
            <label htmlFor="name">Full Name: </label>
            <input onChange={(e) => setName(e.target.value)} placeholder="John Doe" id="name" name="name" />      
            <label htmlFor="email">Email: </label>
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Placeholder@email.com" id="email" name="email" />
            <label htmlFor="password">Password: </label>
            <input value={pass} onChange={(e) => setPass(e.target.value)} type="password" placeholder="********" id="password" name="password" />
            <button type="submit">Register</button>
            <button onClick={() => toLogin()} className='link-button'>Already have an account? Login here.</button>
          </form>
        </div>
      </Popup>
    </div>
  );
}