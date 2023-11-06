import React from 'react';
import './rpgComponents.css'

const Footer = () => {
    const footerStyle = {
        textAlign: 'center',
        padding: '4px',

      };

  const emailStyle = {
    fontStyle: 'italic',
    color: 'lightblue',
    fontSize: '32px',
    fontFamily: 'Papyrus, sans-serif', 
  };

  return (
    <footer style={footerStyle} className="footer">
    <div className='footerImage'>
    <p style={{ color: 'white', fontFamily: 'Papyrus, sans-serif', fontSize: '32px' }}>Contact us at: <a href="mailto:contact@siuargrpg.com" style={emailStyle}>contact@siuargrpg.com</a></p>
    </div>
    </footer>
  );
}

export default Footer;