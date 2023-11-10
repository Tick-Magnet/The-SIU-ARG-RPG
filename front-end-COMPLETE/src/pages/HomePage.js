import React from 'react'
import Navbar from '../components/Navbar.js'


function HomePage ()
{
    return(
    <div className="rpgBackgroundDiv">
        <main>
            <div className="rpgTitle">
                <h1>THE SIU ARG RPG</h1>
            </div>
        
            <div className="rpgTextSection">
                <h2>- Welcome Hero -</h2>
                <p>
                    Embark on a quest as your very own custom RPG hero. 🗡️
                    Scan QR codes to uncover magical items, challenge monstrous creatures, and weave your own legend across campus!
                    To play, login to register an account, create a character, and start scanning QR Codes across campus. 
                </p>
                <h2>- Locations -</h2>
                <p>
                    QR codes have been scattered all across campus and come in three forms. Monsters to fight, treasure to collect, and bonfires to rest at.
                    Simply scan the QR code with your phone's camera and follow the hyperlink to interact with the game.
                </p>
                <h2>Event Details</h2>
                <p>
                    THE SIU ARG RPG servers and QR codes will be maintained from 11/13 - 11/17 (2023). 
                    Join us at 9:00 A.M. Faner Plaza on the 13th for the official launch. 
                    Please always remain mindful when scanning QR codes. When in doubt don't scan.
                </p>
                <h2>Playtesting / Troubleshooting</h2>
                <p>
                    If you don't see the verification email in your inbox look, into your spam/junk folders.
                    If you find a broken QR code, please reach out to us.
                </p>
            </div>
        </main>
    </div>
    );
}

export default HomePage;