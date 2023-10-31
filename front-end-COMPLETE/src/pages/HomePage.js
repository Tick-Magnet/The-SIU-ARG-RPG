import React from 'react'
import Navbar from '../components/Navbar.js'


function HomePage ()
{
    return(
    <div className="rpgBackgroundDiv">
        <h1>SIU ARG RPG Project</h1>
        <main>
            <div className="rpgTextSection">
                <h2>Game Description</h2>
                <p>
                    The SIU ARG RPG is a DND inspired role playing game where players will scan QR codes spread around campus
                    to gather items and fight monsters. After scanning the QR code, the player will either receive the items
                    or have the option of starting the encounter from the QR code.
                </p>
                <h2> Locations </h2>
                <p>
                    QR codes can be found in several locations throughout campus. A map of locations can be displayed by selecting the Map option from
                    the navigation bar
                </p>
                <h2>Length of event </h2>
                <p>
                    The event is expected to take place (release date). The game will be running for one week but may be extended
                    depending on engagement.
                </p>
            </div>
        </main>
    </div>
    );
}

export default HomePage;