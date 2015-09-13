<!DOCTYPE html>
<html lang="en">
  <head>
<!--     <link rel="stylesheet" href="./libs/bootstrap-css-only/css/bootstrap.min.css" /> -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
  </head>
  <body>
    <div class="container" >
      <div class="page-header">
        <h1>Emerald</h1>
      </div>
      <!-- <div class="alert alert-info" role="alert" ng-hide="items && items.length > 0">
        There are no items yet.
      </div>
      <form class="form-horizontal" role="form" ng-submit="addItem(newItem)">
        <div class="form-group" ng-repeat="item in items">
          <div class="checkbox col-xs-9">
            <label>
              <input type="checkbox" ng-model="item.checked" ng-change="updateItem(item)"/> {{item.description}}
            </label>
          </div>
          <div class="col-xs-3">
            <button class="pull-right btn btn-danger" type="button" title="Delete" ng-click="deleteItem(item)">
              <span class="glyphicon glyphicon-trash"></span>
            </button>
          </div>
        </div>
        <hr />
        <div class="input-group">
          <input type="text" class="form-control" ng-model="newItem" placeholder="Enter the description..." />
          <span class="input-group-btn">
            <button class="btn btn-default" type="submit" ng-disabled="!newItem" title="Add">
              <span class="glyphicon glyphicon-plus"></span>
            </button>
          </span>
        </div>
      </form> -->
      <div class="col-md-6">
      	<form class="col-md-12" action="auth" method="post">
		    <div class="form-group">
		        <input type="email" name="username" id="username" class="form-control input-lg" placeholder="Email" required />
		    </div>
		    <div class="form-group">
		        <input type="password" name="password" id="password" class="form-control input-lg" placeholder="Password" required />
		    </div>
		    <div class="form-group">
		        <button class="btn btn-primary btn-lg btn-block" type="submit">Sign In</button>
		        <span><a href="#">Need help?</a></span>
		        <span class="pull-right"><a href="#">New Registration</a></span>
		    </div>
		</form>
      </div>
    </div>
<!--     <script type="text/javascript" src="./libs/lodash/dist/lodash.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-resource.min.js"></script>
    <script type="text/javascript" src="./libs/angular-spring-data-rest/dist/angular-spring-data-rest.min.js"></script>
    <script type="text/javascript" src="./app/app.js"></script>
    <script type="text/javascript" src="./app/controllers.js"></script>
    <script type="text/javascript" src="./app/services.js"></script> -->
  </body>
</html>