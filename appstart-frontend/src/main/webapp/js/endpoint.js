/**
 * App Engine Cloud Endpoint JavaScript initilization
 * The cloud Endpoint token obtained expires in one hour
 * this script will refresh the token if it has expired.
 */
var $ = document.querySelector.bind(document);

var appstart = appstart || {};

// cloud endpoints url
appstart.root = '//' + window.location.host + '/_ah/api';

// clientId, replace with yours
appstart.clientId = '677918637620-k761b9omn72ffrtpebhm59spop9k64b3.apps.googleusercontent.com';

appstart.functions = {
	callbacks: []
};

appstart.validateEndpointLoaded = function () {
	 if(!gapi.client.appstart) {
		 console.log('Likely endpoint discovery failed with 404 trying again...');
		 gapi.client.load('appstart', 'v1', appstart.validateEndpointLoaded , ROOT);

	 }
}

appstart.init = function() {
	var apisToLoad;
	var callback = function() {
		if (--apisToLoad == 0) {
			appstart.signin(true, appstart.userAuthed);
		}
	}

	apisToLoad = 1; // must match number of calls to gapi.client.load()
	gapi.client.load('appstart', 'v1', appstart.validateEndpointLoaded, appstart.root);
	gapi.client.load('oauth2', 'v2', callback);
};

// the api client needs a global variable
var apiClientInit = appstart.init;

appstart.signin = function(mode, callback) {
	gapi.auth.authorize({
		client_id : appstart.clientId,
		scope : "https://www.googleapis.com/auth/userinfo.email",
		immediate : mode,
		response_type : 'token id_token'
	}, callback);

};

/**
 * token = {
 *  _aa: "1"
	access_token: "eyJhbGciOiJSUzI1NiIasdfadfafmQzOGNjYTA2MWJjNTFhNTVhMDRiYzI0YzE2MTIxODQifQ..."
	client_id: "231728023959.apps.googleusercontent.com"
	cookie_policy: undefined
	expires_at: "1377248594"
	expires_in: "3600"
	g_user_cookie_policy: undefined
	id_token: "eyJhbGciOiJSUzI1NiIsImtasfasfasdfA2MWJjNTFhNTVhMDRiYzI0YzE2MTIxODQifQ....."
	issued_at: "1377244994"
	response_type: "token id_token"
	scope: "https://www.googleapis.com/auth/userinfo.email"
	state: ""
	token_type: "Bearer"
 }
 */
appstart.userAuthed = function() {
	var request = gapi.client.oauth2.userinfo.get().execute(function(resp) {
		console.log(resp);
		console.log(resp.code);
		if (!resp.code) {
			var token = gapi.auth.getToken();
			token.access_token = token.id_token;
			gapi.auth.setToken(token);
			// User is signed in, call my Endpoint
			if(appstart.functions && appstart.functions.callbacks &&
				(appstart.functions.callbacks.length > 0)) {

				for(var i = 0; i < appstart.functions.callbacks.length; i++ ) {
					appstart.functions.callbacks[i]();
				}
				// clear it
				appstart.functions.callbacks = [];
			}

		} else {
			// this the OAuth button that the user needs to click on
			if($('.auth')) {
				$('.auth').style.display = '';
			}
		}

	});
};

appstart.auth = function () {
	if($('.auth')) {
		$('.auth').style.display = 'none';
	}
	appstart.signin(false, appstart.userAuthed);
};


appstart.refreshOAuthTokenIfExpired = function() {

	if(!appstart.isOAuthTokenValid()) {
		// token has expired then we need to refresh it
		appstart.signin(false, appstart.userAuthed);
	}
};

appstart.isOAuthTokenValid = function() {
	var token = gapi.auth.getToken();
	return (token && token.access_token && token.id_token);
};
