import axios from 'axios'

const apiURL = 'http://localhost:8080/test';
//axios.defaults.headers.get['Acess-Control-Allow-Origin'] = '*';

class RPGServerProxy
{
	
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
}

export default new RPGServerProxy();
