import React from 'react';
import './rpgComponents.css'

const Footer = () => {

  var musicSelector = Math.floor(Math.random() * 3);
  var musicSrc = -1;
  var isPlaying = false;

  const footerStyle = {
    textAlign: 'center',
    padding: '4px',
    margin: '4px',
  };

  const emailStyle = {
    fontStyle: 'italic',
    color: 'lightblue',
    fontSize: '32px',
    fontFamily: 'Papyrus, sans-serif', 
  };

  switch (musicSelector) 
    {
        case 0:
            musicSrc = "/Music/KaskaskiaKings.mp3";
            break;
        case 1:
            musicSrc = "/Music/MurdalesMoment.mp3";
            break;
        case 2:
            musicSrc = "/Music/SalukiSprint.mp3";
            break;
        default:
            console.log("Music went wrong bro");
    }

    function playAudio()
    {
      const myAudio = document.getElementsByClassName("myMusic")[0]
  
      if (isPlaying) { 
        myAudio.pause(); 
        isPlaying = false;
      } else { 
        myAudio.play(); 
        isPlaying = true;
      } 
    }

  return (
    <footer style={footerStyle} className="footer">
      <div className='footerImage'>

        <audio className="myMusic" loop>
          <source src={musicSrc}/>
        </audio>

        <p style={{color:"white",fontFamily:"Papyrus, sans-serif",fontSize:"32px"}}>
          Contact us at: <a href="mailto:contact@siuargrpg.com" style={emailStyle}>contact@siuargrpg.com</a>
        </p>

        <button className="musicButt" onClick={playAudio}>Toggle Music</button>

      </div>
    </footer>
  );
}

export default Footer;