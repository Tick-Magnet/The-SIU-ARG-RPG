# The SIU ARG RPG
Web based RPG where game content is delivered through QR codes. Hosted on AWS for a two week on-campus event

<p align="center">
  <img src="/RPGServer/src/main/resources/images/combat1.png">
</p>

## Features
- Players able to scan QR codes to unlock items or start combat encounters
- Players able to create a character upon signing up
- Admin dashboard on front end for creating QR code posters and accessing the command shell
- Command shell allowing any method inside the ShellCommands class to be invoked by previleged users from the front-end. Some methods include banning players, changing user roles, and overriding account verification
- Detailed encounter system allowing different dialgue and combat steps to be strung together in elaborate ways
- Encounters and other game content parsed from JSON files, allowing content to be created dynamically and without altering back-end code
- Basic flood protection on back end API. Rate limits applied to API calls, blocking IP addresses for a time after the limit is exceeded

<p align="center">
  <img src="/QRCodes/tower_one.png">
</p>

## Technology used
### Back-end: Java, Spring Boot, SQL/Hibernate
### Front-end: HTML/CSS, React, Javascript

## Contributors
<a href="https://github.com/Tick-Magnet/The-SIU-ARG-RPG/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Tick-Magnet/The-SIU-ARG-RPG" />
</a>
