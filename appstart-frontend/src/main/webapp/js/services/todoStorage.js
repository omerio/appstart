/*global angular */

/**
 * Services that persists and retrieves todos from localStorage or a backend API
 * if available.
 *
 * They both follow the same API, returning promises for all changes to the
 * model.
 */
angular.module('todomvc')
	.factory('todoStorage', function ($q, $http, $injector, toastr) {
		'use strict';

		// Detect if an API backend is present. If so, return the API module, else
		// hand off the localStorage adapter
		// return $http.get('/api')
		// 	.then(function () {
		// 		return $injector.get('api');
		// 	}, function () {
		// 		return $injector.get('localStorage');
		// 	});
		/*var deferred = $q.defer();
  	var promise = deferred.promise;
		promise.then(function() { 	return $injector.get('api'); });
		return promise;*/
		var toast = toastr.info('Loading please wait...', 'Information', {
			timeOut: 3000
		});

		return $http.get('/api/todo')
			.then(function () {
				return $injector.get('api');
			}, function () {
				return $injector.get('localStorage');
			}
		);
	})

	.factory('api', function ($resource, $q, toastr) {
		'use strict';

		var store = {
			todos: [],

			toastrTimeout: 3000,

			endpointType: 'jerseyrest',

			api: $resource('/api/todo/:id', null,
				{
					update: { method:'PUT' }
				}
			),

			isEndpointLoaded: function() {
				var loaded = false;
				if(gapi.client.appstart) {
					loaded = true;
				} else {
					toastr.error('Cloud Endpoints client is not loaded, please refresh the page.', 'Error', { timeOut: store.toastrTimeout });
				}
				return loaded;
			},

			callCloudEndpoint: function(endpoint, callback, skipAuth) {

				if (store.isEndpointLoaded()) {
					// call the cloud endpoint
					if(skipAuth || appstart.isOAuthTokenValid()) {

						endpoint.execute(callback);

					} else {
						// token is expired, renew and try again
						appstart.functions.callbacks = [function() {
							endpoint.execute(callback);
						}];

						appstart.refreshOAuthTokenIfExpired();
					}
				}
			},

			/*clearCompleted: function () {
				var originalTodos = store.todos.slice(0);

				var completeTodos = [];
				var incompleteTodos = [];
				store.todos.forEach(function (todo) {
					if (todo.completed) {
						completeTodos.push(todo);
					} else {
						incompleteTodos.push(todo);
					}
				});

				angular.copy(incompleteTodos, store.todos);

				return store.api.delete(function () {
					}, function error() {
						angular.copy(originalTodos, store.todos);
					});
			},*/

			delete: function (todo) {
				var originalTodos = store.todos.slice(0);

				store.todos.splice(store.todos.indexOf(todo), 1);

				var promise;
				var auth = false;

				switch(store.endpointType) {

					case 'jerseyrest':
						promise = store.api.delete({ id: todo.id },
							function () {
							}, function error() {
								angular.copy(originalTodos, store.todos);
							});

					break;

					case 'cloudendpointsauth':
						auth = true;

					case 'cloudendpoints':
						var deferred = $q.defer();

						store.callCloudEndpoint(gapi.client.appstart.todos.delete({'id': todo.id, 'auth': auth}), function(resp) {
								if (resp.error) {
									toastr.error('A server error has occured please try again. ' + resp.error.message, 'Error', { timeOut: store.toastrTimeout });
									angular.copy(originalTodos, store.todos);
								}

								deferred.resolve(store.todos);
						}, true);

						promise = deferred.promise;

					break;

				};

				return promise;
			},

			get: function ($scope) {
				var promise;
				var auth = false;

				switch(store.endpointType) {

					case 'jerseyrest':
						promise = store.api.query(function (resp) {
							angular.copy(resp, store.todos);
						});
					break;

					case 'cloudendpointsauth':
						auth = true;

					case 'cloudendpoints':
						var deferred = $q.defer();

						store.callCloudEndpoint(gapi.client.appstart.todos.list({'auth': auth}), function(resp) {
								if (resp.error) {
									toastr.error('A server error has occured please try again. ' + resp.error.message, 'Error', { timeOut: store.toastrTimeout });
								} else {
									angular.copy(resp.items, store.todos);
								}
								deferred.resolve(store.todos);

								// had to pass the $scope as it wasn't refreshing when store.todos changes
								if($scope) {
									 $scope.$apply();
								}
						}, true);

						promise = deferred.promise;

					break;

				};

				return promise;
			},

			insert: function (todo) {
				var originalTodos = store.todos.slice(0);

				var promise;
				var auth = false;

				switch(store.endpointType) {

					case 'jerseyrest':
						promise = store.api.save(todo,
							function success(resp) {
								todo.id = resp.id;
								store.todos.push(todo);

							}, function error() {
								angular.copy(originalTodos, store.todos);
						}).$promise;

					break;

					case 'cloudendpointsauth':
						auth = true;

					case 'cloudendpoints':
						var deferred = $q.defer();

						store.callCloudEndpoint(gapi.client.appstart.todos.create({'auth': auth, 'resource': todo}), function(resp) {
								if (resp.error) {
									toastr.error('A server error has occured please try again. ' + resp.error.message, 'Error', { timeOut: store.toastrTimeout });
									angular.copy(originalTodos, store.todos);

								} else {
									todo.id = resp.id;
									store.todos.push(todo);
								}
								deferred.resolve(store.todos);
						}, true);

						promise = deferred.promise;

					break;

				};

				return promise;
			},

			put: function (todo) {

				var promise;
				var auth = false;

				switch(store.endpointType) {

					case 'jerseyrest':
						promise = store.api.update({ id: todo.id }, todo).$promise;

					break;

					case 'cloudendpointsauth':
						auth = true;

					case 'cloudendpoints':
						var deferred = $q.defer();

						store.callCloudEndpoint(gapi.client.appstart.todos.update({'id': todo.id, 'resource': todo, 'auth': auth}), function(resp) {
								if (resp.error) {
									toastr.error('A server error has occured please try again. ' + resp.error.message, 'Error', { timeOut: store.toastrTimeout });
									deferred.reject(resp);
								} else {
									deferred.resolve(resp);
								}

						}, true);

						promise = deferred.promise;

					break;

				};

				return promise;
			}
		};

		return store;
	})

	.factory('localStorage', function ($q) {
		'use strict';

		var STORAGE_ID = 'todos-angularjs';

		var store = {
			todos: [],

			_getFromLocalStorage: function () {
				return JSON.parse(localStorage.getItem(STORAGE_ID) || '[]');
			},

			_saveToLocalStorage: function (todos) {
				localStorage.setItem(STORAGE_ID, JSON.stringify(todos));
			},

			clearCompleted: function () {
				var deferred = $q.defer();

				var completeTodos = [];
				var incompleteTodos = [];
				store.todos.forEach(function (todo) {
					if (todo.completed) {
						completeTodos.push(todo);
					} else {
						incompleteTodos.push(todo);
					}
				});

				angular.copy(incompleteTodos, store.todos);

				store._saveToLocalStorage(store.todos);
				deferred.resolve(store.todos);

				return deferred.promise;
			},

			delete: function (todo) {
				var deferred = $q.defer();

				store.todos.splice(store.todos.indexOf(todo), 1);

				store._saveToLocalStorage(store.todos);
				deferred.resolve(store.todos);

				return deferred.promise;
			},

			get: function () {
				var deferred = $q.defer();

				angular.copy(store._getFromLocalStorage(), store.todos);
				deferred.resolve(store.todos);

				return deferred.promise;
			},

			insert: function (todo) {
				var deferred = $q.defer();

				store.todos.push(todo);

				store._saveToLocalStorage(store.todos);
				deferred.resolve(store.todos);

				return deferred.promise;
			},

			put: function (todo, index) {
				var deferred = $q.defer();

				store.todos[index] = todo;

				store._saveToLocalStorage(store.todos);
				deferred.resolve(store.todos);

				return deferred.promise;
			}
		};

		return store;
	});
