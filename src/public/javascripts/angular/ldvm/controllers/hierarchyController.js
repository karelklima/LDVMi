define(['angular', './controllers'], function (ng) {
    'use strict';

    return ng.module('ldvm.controllers')
        .controller('Hierarchy',
        [
            '$scope',
            '$routeParams',
            'Visualization',
            function ($scope,
                      $routeParams,
                      visualization) {

                var id = $scope.id = $routeParams.id;
                if (!id) {
                    return;
                }

                $scope.visType = $routeParams.type;

                $scope.schemes = [];
                $scope.scheme = null;
                $scope.queryingDataset = null;

                var promise = visualization.skos.schemes(id);
                $scope.queryingDataset = true;
                promise.then(function (schemes) {
                    $scope.schemes = schemes;
                    $scope.queryingDataset = false;
                });

                $scope.uri = function (scheme) {
                    return "#/hierarchy/" + $scope.visType + "/" + $scope.id + "/?uri=" + encodeURIComponent(scheme.uri);
                };

                $scope.embedUrl = function (visType, includeHost) {
                    return (includeHost ? "http://" + document.location.host : "") + "/visualize/embed/" + visType + "/" + $scope.id + "?schemeUri=" + encodeURIComponent($routeParams.uri);
                };

                $scope.loadScheme = function (schemeUri) {
                    $scope.queryingDataset = true;
                    var schemePromise = visualization.skos.scheme(id, schemeUri);
                    schemePromise.then(function (scheme) {
                        $scope.queryingDataset = false;
                        $scope.scheme = scheme;
                    });
                };

                $scope.switch = function (type) {
                    $scope.visType = type;
                };

                $scope.visualizations = [
                    {
                        key: 'sunburst',
                        label: 'Sunburst'
                    },
                    {
                        key: 'treemap',
                        label: 'Treemap'
                    },
                    {
                        key: 'bilevel',
                        label: 'BiLevel'
                    },
                    {
                        key: 'packLayout',
                        label: 'Pack Layout'
                    },
                    {
                        key: 'force',
                        label: 'Force Layout'
                    },
                    {
                        key: 'cluster',
                        label: 'Rotating Cluster'
                    },
                    {
                        key: 'partition',
                        label: 'Partition Layout'
                    },
                    {
                        key: 'tree',
                        label: 'Tree'
                    },
                    {
                        key: 'radialTree',
                        label: 'Radial tree'
                    }
                ];

                if ($routeParams.uri) {
                    $scope.loadScheme($routeParams.uri);
                }

            }]);
});