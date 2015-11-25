

var app = angular.module('bikedashapp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/list_systems.html',
        controller: 'ListCtrl'
    }).when('/create', {
        templateUrl: 'views/create_system.html',
        controller: 'CreateCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.controller('ListCtrl', function ($scope, $http) {
    $http.get('/api/v1/bikesystems').success(function (data) {
        $scope.metadata = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
});

app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.todo = {
        done: false
    };

    $scope.createSystem = function () {
        $http.post('/api/v1/station/metadata', $scope.m).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});