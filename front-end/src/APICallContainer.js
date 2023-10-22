
import axios from 'axios'
import Cookies from 'universal-cookie';

const apiURL = 'http://localhost:8080';
//axios.defaults.headers.get['Acess-Control-Allow-Origin'] = '*';
const cookies = new Cookies();

class APICallContainer
{
	
	checkToken(inputUsername)
	{
		var url ="";
		url = url.concat(apiURL, "/checktoken");
		axios.post(url,
		{
			username: inputUsername,
			token: cookies.get('sessionToken')
		})
		.then(function (response)
		{
			console.log(response.data);
		});
	}
	
	testAPI()
	{
		var url = "";
		//url.concat(apiURL, "/test");
		axios.get(apiURL).then(function (response)
		{
			console.log(response.data);
			console.log(response.status);	
		});
		return "test"
	}
	
	register(inputUsername, inputEmail, inputPassword)
	{
		var url = "";
		url = url.concat(apiURL, "/register");
		console.log(url);
		axios.post(url,
		{
			username: inputUsername,
			email: inputEmail,
			password: inputPassword
		})
		.then(function (response)
		{
			console.log(response.data);
			return response.data;
		});
	}
	
	login(inputUsername, inputPassword)
	{
		//this.checkToken(inputUsername);
		var url = "";
		url = url.concat(apiURL, "/login");
		axios.post(url,
		{
			username: inputUsername,
			password: inputPassword
		})
		.then(function (response)
		{
			//Store session token as a cookie
			cookies.set('sessionToken', response.data.token, {path:'/', maxAge:82800});
			console.log(response.data);
			return response.data;
		});
	}
	
	createCharacter(inputUsername, inputToken, inputCharacterClass, inputCharacterRace)
	{
		//this.checkToken(inputUsername);
		var url = "";
		url = url.concat(apiURL, "/createCharacter");
		axios.post(url,
		{
			username: inputUsername,
			token: inputToken,
			characterClass: inputCharacterClass,
			characterRace: inputCharacterRace
		})
		.then(function (response)
		{
			console.log(response.data);
			return response.data;
		});
	}

	createCharacter(inputUsername, inputToken, inputCharacterClass, inputCharacterRace)
    {
    		//this.checkToken(inputUsername);
    		var url = "";
    		url = url.concat(apiURL, "/createCharacter");
    		axios.post(url,
    		{
    			username: inputUsername,
    			token: inputToken,
    			characterClass: inputCharacterClass,
    			characterRace: inputCharacterRace
    		})
    		.then(function (response)
    		{
    			console.log(response.data);
    			return response.data;
    		});
    }
    postEncounterUpdate(inputUsername, inputToken, inputSelectedChoice)
        {
        		//this.checkToken(inputUsername);
        		var url = "";
        		url = url.concat(apiURL, "/postEncounterUpdate");
        		axios.post(url,
        		{
        			username: inputUsername,
        			token: inputToken,
        			selectedChoice: inputSelectedChoice
        		})
        		.then(function (response)
        		{
        			console.log(response.data);
        			return response.data;
        		});
        }

    redeemQR(inputUsername, inputToken, inputUUID)
        {
        		//this.checkToken(inputUsername);
        		var url = "";
        		url = url.concat(apiURL, "/redeemQR");
        		axios.post(url,
        		{
        			username: inputUsername,
        			token: inputToken,
        			uuid: inputUUID
        		})
        		.then(function (response)
        		{
        			console.log(response.data);
        			return response.data;
        		});
        }
        createQRCode(inputUsername, inputToken, inputType, inputColorType, inputItemDefinitionPath, inputEncounterDefinitionPath)
                {
                		//this.checkToken(inputUsername);
                		var url = "";
                		url = url.concat(apiURL, "/createCharacter");
                		axios.post(url,
                		{
                			username: inputUsername,
                			token: inputToken,
                			type: inputType,
                			colorType: inputColorType,
                			itemDefinitionPath: inputItemDefinitionPath,
                			encounterDefinitionPath: inputEncounterDefinitionPath
                		})
                		.then(function (response)
                		{
                			console.log(response.data);
                			return response.data;
                		});
                }

    getInventory(inputUsername, inputToken)
        {
        		//this.checkToken(inputUsername);
        		var url = "";
        		url = url.concat(apiURL, "/getInventory");
        		axios.post(url,
        		{
        			username: inputUsername,
        			token: inputToken
        		})
        		.then(function (response)
        		{
        			console.log(response.data);
        			return response.data;
        		});
        }
    inspectItemSlot(inputUsername, inputToken, inputIndex)
        {
        		//this.checkToken(inputUsername);
        		var url = "";
        		url = url.concat(apiURL, "/getInventory");
        		axios.post(url,
        		{
        			username: inputUsername,
        			token: inputToken,
        			index: inputIndex
        		})
        		.then(function (response)
        		{
        			console.log(response.data);
        			return response.data;
        		});
        }
 equipItem(inputUsername, inputToken, inputIndex, inputItemType)
        {
        		//this.checkToken(inputUsername);
        		var url = "";
        		url = url.concat(apiURL, "/getInventory");
        		axios.post(url,
        		{
        			username: inputUsername,
        			token: inputToken,
        			index: inputIndex,
        			itemType: inputItemType
        		})
        		.then(function (response)
        		{
        			console.log(response.data);
        			return response.data;
        		});
        }
  sellItem(inputUsername, inputToken, inputIndex)
         {
         		//this.checkToken(inputUsername);
         		var url = "";
         		url = url.concat(apiURL, "/getInventory");
         		axios.post(url,
         		{
         			username: inputUsername,
         			token: inputToken,
         			index: inputIndex
         		})
         		.then(function (response)
         		{
         			console.log(response.data);
         			return response.data;
         		});
         }
}

export default new APICallContainer();
