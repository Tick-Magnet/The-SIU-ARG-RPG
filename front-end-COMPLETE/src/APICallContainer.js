
import axios from 'axios'
import Cookies from 'universal-cookie';

const apiURL = 'http://localhost:8080';
//axios.defaults.headers.get['Acess-Control-Allow-Origin'] = '*';
const cookies = new Cookies();

class APICallContainer
{

	async checkToken(inputUsername, inputToken)
	{
		var url ="";
		url = url.concat(apiURL, "/checktoken");

		axios.post(url,
		{
			username: inputUsername,
			token: inputToken

		})
		.then(function (response)
		{
			console.log(response.data);
			return response.data;
		}).catch(function (error)
		{
		    console.log("Backend network error");
		    console.log(error.toJSON());
		});
	}

    async getLoginInfo()
    {
        var output = {loggedIn: false};
        var currentToken = cookies.get("sessionToken");
        var currentUsername = cookies.get("username");
        var url ="";
		url = url.concat(apiURL, "/checktoken");
		//console.log(currentUsername+ currentToken);
		if(currentToken != null && currentToken != "undefined" && currentUsername != null)
		{
        var postResult = await axios.post(url,
		{
			username: currentUsername,
			token: currentToken

		})
		.then(function (response)
		{
			//console.log(response.data);

			return response.data;
		}).catch(function (error)
		{
		    console.log("Backend network error");
		    console.log(error.toJSON());
		});
        if(postResult.valid == true)
        {
            output.loggedIn = true;
            output.username = currentUsername;
            output.sessionToken = currentToken;
        }
        }

		return output;
    }
	
	async register(inputUsername, inputEmail, inputPassword)
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
	/*
	async login(inputUsername, inputPassword)
	{
		//this.checkToken(inputUsername);
		var url = "";
		url = url.concat(apiURL, "/login");
		var result = await axios.post(url,
		{
			username: inputUsername,
			password: inputPassword
		})
		.then(function (response)
		{
		    if(response.data.valid == true){
			//Store session token as a cookie
			console.log(response.data.token);
			cookies.set('sessionToken', response.data.token, {path:'/', maxAge:82800});
			cookies.set('username', inputUsername, {path:'/', maxAge:82800});
            }
			console.log(response.data);
			return response.data;
		});
        console.log(result.username +"RESULT");
		return result;
	}
    */
async login(inputUsername, inputPassword)
	{
		//this.checkToken(inputUsername);
		var output = {loggedIn: false};
		var url = "";
		url = url.concat(apiURL, "/login");
		var result = await axios.post(url,
		{
			username: inputUsername,
			password: inputPassword
		});
		if(result.data.valid == true){
        	//Store session token as a cookie
            console.log(result.data.token);
            cookies.set('sessionToken', result.data.token, {path:'/', maxAge:82800});
            cookies.set('username', inputUsername, {path:'/', maxAge:82800});
            output.username = inputUsername;
            output.sessionToken = result.data.token;
            output.loggedIn = true;
        }
        console.log(output);


        return output;
	}

	async createCharacter(inputUsername, inputToken, inputCharacterClass, inputCharacterRace)
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

	async createCharacter(inputUsername, inputToken, inputCharacterClass, inputCharacterRace)
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
   async postEncounterUpdate(inputUsername, inputToken, inputSelectedChoice, inputEncounterID)
        {
        		//this.checkToken(inputUsername);
        		var url = "";
        		url = url.concat(apiURL, "/postEncounterUpdate");
        		axios.post(url,
        		{
        			username: inputUsername,
        			token: inputToken,
        			selectedChoice: inputSelectedChoice,
        			encounterID: inputEncounterID
        		})
        		.then(function (response)
        		{
        			console.log(response.data);
        			return response.data;
        		});
        }

    async redeemQR(inputUsername, inputToken, inputUUID)
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
       async createQRCode(inputUsername, inputToken, inputType, inputColorType, inputItemDefinitionPath, inputEncounterDefinitionPath)
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

    async getInventory(inputUsername, inputToken)
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
    async inspectItemSlot(inputUsername, inputToken, inputIndex)
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
 async equipItem(inputUsername, inputToken, inputIndex, inputItemType)
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
  async sellItem(inputUsername, inputToken, inputIndex)
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
