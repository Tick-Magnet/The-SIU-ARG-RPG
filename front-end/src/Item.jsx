import Popup from './components/Popup';
import APICallContainer from './APICallContainer';
import {useState} from 'react';

export const Item = (props) => {
  const [loginPopup, setLoginPopup] = useState(false);
  const [registerPopup, setRegisterPopup] = useState(false);
  const [itemPopup, setItemPopup] = useState(false);
  const [mapPopup, setMapPopup] = useState(false);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [pass, setPass] = useState('');

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

  return (
    <div className="Item">
        <input type='image' src='./Images/compass.png' alt='Map' className='compass' onClick={() => setMapPopup(true)}/>
        <input type='image' src='./Images/profile.jfif' alt='Login' className='loginbutt' onClick={() => setLoginPopup(true)}/>
      <main>
        <input type='image' src='./Images/chest.png' alt='Open Chest' className='chest' onClick={() => setItemPopup(true)}/>
        <h1 className='prompt'>Tap To Open</h1>
      </main>

      <Popup trigger={itemPopup} setTrigger={setItemPopup}>
        <h3>You found a Bronze Sword!</h3>
        <img src='' alt='Bronze sword' />
      </Popup>

      <Popup trigger={mapPopup} setTrigger={setMapPopup}>
        <img src='./Images/Map.png' alt='Map of QR codes' />
      </Popup>

      <Popup trigger={loginPopup} setTrigger={setLoginPopup}>
        <div className='auth-form-container'>
          <h1>Log In</h1>
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