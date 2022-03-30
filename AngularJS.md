# AngularJS

## 模块

模块定义了一个应用程序，模块是应用程序的容器，也是应用控制器的容器。

通过 AngularJS 的 **angular.module** 函数来创建模块，可以在 AngularJS 应用中添加控制器，指令，过滤器等 

```html
//"myApp"参数对应执行应用的HTML元素

<div ng-app="myApp" ng-controller="myCtrl">
    {{ firstName + " " + lastName }}
</div>

<script>
    var app = angular.module("myApp", []);
    app.controller("myCtrl", function($scope) {
        $scope.firstName = "John";
        $scope.lastName = "Doe";
    });
</script>
```

JavaScript 中应避免使用全局函数。因为他们很容易被其他脚本文件覆盖，但是 AngularJS 模块让所有函数的作用域都在该模块下。

## 指令

**ng-app** 指令初始化一个 AngularJS 应用程序。

**ng-init** 指令初始化应用程序数据。

**ng-model** 指令把元素值（比如输入域的值）绑定到应用程序，也就是将输入域的值与 AngularJS 创建的变量绑定 

```html
//AngularJS 中的数据绑定表达式{{ firstName }}，通过ng-model，同步了 AngularJS 表达式与 AngularJS 数据

<div ng-app="" ng-init="quantity=1;price=5">
    <h2>价格计算器</h2>
    数量： <input type="number" ng-model="quantity">
    价格： <input type="number" ng-model="price">
    <p><b>总价：</b> {{ quantity * price }}</p>
</div>
```



**ng-repeat** 指令会重复一个 HTML 元素 

```html
<div ng-app="" ng-init="names=['Jani','Hege','Kai']">
    <p>使用 ng-repeat 来循环数组</p>
    <ul>
        <li ng-repeat="x in names">
            {{ x }}
        </li>
    </ul>
</div>
```

```html
<div ng-app="" ng-init="names=[
                        {name:'Jani',country:'Norway'},
                        {name:'Hege',country:'Sweden'},
                        {name:'Kai',country:'Denmark'}]">
    <p>循环对象：</p>
    <ul>
        <li ng-repeat="x in names">
            {{ x.name + ', ' + x.country }}
        </li>
    </ul>
</div>
```



**ng-option** 指令来创建一个下拉列表，列表项通过对象和数组循环输出，**ng-init** 设置默认选中值

```html
//Select(选择框)

<div ng-app="myApp" ng-controller="myCtrl">
    <select ng-init="selectedName = names[0]" ng-model="selectedName" ng-options="x for x in names">
    </select>
</div>

<script>
    var app = angular.module('myApp', []);
    app.controller('myCtrl', function($scope) {
        $scope.names = ["Google", "Runoob", "Taobao"];
    });
</script>
```



AngularJS 也为 HTML DOM 元素的属性提供了绑定应用数据的指令 

**ng-disabled** 指令直接绑定应用程序数据到 HTML 的 disabled 属性。 

```html
<div ng-app="" ng-init="mySwitch=true">
    <p>
        <button ng-disabled="mySwitch">点我!</button>
    </p>
    <p>
        <input type="checkbox" ng-model="mySwitch">按钮
    </p>
    <p>
        {{ mySwitch }}
    </p>
</div>
```

checkbox类型为选中框，选中时mySwitch为true，`<button disabled>点我！</button> ` 按钮不可用

未选中时mySwitch为false，`<button >点我！</button> ` 按钮可用



**ng-show** 和 **ng-hide** 指令隐藏或显示一个HTML元素，根据 **value** 的值来显示（隐藏）HTML元素，可以使用表达式来计算布尔值（ true 或 false）

```html
<div ng-app="" ng-init="hour=13">
    <p ng-show="hour > 12">我是可见的。</p>
</div>
```





**自定义指令**

使用 **.directive** 函数来添加自定义的指令 

```html
//使用驼峰法来命名一个指令， runoobDirective, 但在使用它时需要以 - 分割, runoob-directive:

<body ng-app="myApp">
    <runoob-directive></runoob-directive>
    <script>
        var app = angular.module("myApp", []);
        app.directive("runoobDirective", function() {
            return {
                template : "<h1>自定义指令!</h1>"
            };
        });
    </script>
</body>
```

你可以通过以下方式来调用指令：

- 元素名 `<runoob-directive></runoob-directive> `
- 属性    `<div runoob-directive></div> `
- 类名    `<div class="runoob-directive"></div> `
- 注释    `<!-- directive: runoob-directive --> `

可以限制指令只能通过特定的方式来调用 

**restrict** 值可以是以下几种:

- `E` 作为元素名使用
- `A` 作为属性使用
- `C` 作为类名使用
- `M` 作为注释使用

**restrict** 默认值为 `EA`, 即可以通过元素名和属性名来调用指令。

```js
//通过添加 restrict 属性,并设置值为 "A", 来设置指令只能通过属性的方式来调用

var app = angular.module("myApp", []);
app.directive("runoobDirective", function() {
    return {
        restrict : "A",
        template : "<h1>自定义指令!</h1>"
    };
});
```



## 控制器

```html
<div ng-app="myApp" ng-controller="personCtrl">
    名: <input type="text" ng-model="firstName"><br>
    姓: <input type="text" ng-model="lastName"><br>
    <br>
    姓名: {{fullName()}}
</div>

<script>
    var app = angular.module('myApp', []);
    app.controller('personCtrl', function($scope) {
        $scope.firstName = "John";
        $scope.lastName = "Doe";
        $scope.fullName = function() {
            return $scope.firstName + " " + $scope.lastName;
        }
    });
</script>
```

**ng-app** 定义AngularJS应用程序，应用程序在 `<div>` 内运行。 

**ng-controller="personCtrl"** 属性是一个 AngularJS指令，用于定义一个控制器。 `personCtrl`函数是一个 JavaScript函数。 

**ng-model** 指令绑定输入域到控制器的属性（firstName 和 lastName）。 



在AngularJS中， **$scope** 是一个应用对象(属于应用变量和函数)。$scope（相当于作用域、控制范围）用来保存AngularJS Model(模型)的对象。

控制器在作用域中创建了两个属性 (**firstName** 和 **lastName**)。 

## 过滤器

```html
//通过一个管道字符（|）和一个过滤器添加到表达式中
//uppercase过滤器将字符串格式化为大写，lowercase过滤器将字符串格式化为小写

<div ng-app="myApp" ng-controller="personCtrl">
    <p>姓名为 {{ lastName | uppercase }}</p>
</div>
```



```html
//currency 过滤器将数字格式化为货币格式

<div ng-app="myApp" ng-controller="costCtrl">
    <input type="number" ng-model="quantity">
    <input type="number" ng-model="price">
    <p>总价 = {{ (quantity * price) | currency }}</p>
</div>
```



```html
//通过一个管道字符（|）和一个过滤器添加到指令中
//orderBy 过滤器根据表达式排列数组

<div ng-app="myApp" ng-controller="namesCtrl">
    <ul>
        <li ng-repeat="x in names | orderBy:'country'">
            {{ x.name + ', ' + x.country }}
        </li>
    </ul>
</div>
```



```html
//输入过滤器可以通过一个管道字符（|）和一个过滤器添加到指令中，该过滤器后跟一个冒号和一个模型名称。
//filter 过滤器从数组中选择一个子集

<div ng-app="myApp" ng-controller="namesCtrl">
    <p><input type="text" ng-model="test"></p>
    <ul>
        <li ng-repeat="x in names | filter:test | orderBy:'country'">
            {{ (x.name | uppercase) + ', ' + x.country }}
        </li>
    </ul>
</div>
```



```js
//自定义一个过滤器 reverse，将字符串反转

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
    $scope.msg = "Runoob";
});
app.filter('reverse', function() { //可以注入依赖
    return function(text) {
        return text.split("").reverse().join("");
    }
});
```

## 服务

在 AngularJS 中，服务是一个函数或对象，AngularJS 内建了30多个服务。

**$location** 服务，它可以返回当前页面的 URL 地址。 

```js
// $location 服务是作为一个参数传递到 controller 中。如果要使用它，需要在 controller 中定义

var app = angular.module('myApp', []);
app.controller('customersCtrl', function($scope, $location) {
    $scope.myUrl = $location.path();	//返回"/actual/path"
});
```

**$http** 服务向服务器发送请求，应用响应服务器传送过来的数据 

```js
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
    $http.get("welcome.htm").then(function (response) {
        $scope.myWelcome = response.data;
    });
});
```

**$timeout 服务** 对应了 JS **window.setTimeout** 函数 

```js
//两秒后显示信息 

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $timeout) {
    $scope.myHeader = "Hello World!";
    $timeout(function () {
        $scope.myHeader = "How are you today?";
    }, 2000);
});
```

**$interval** 服务对应了 JS **window.setInterval** 函数 

```js
//每一秒显示信息

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $interval) {
    $scope.theTime = new Date().toLocaleTimeString();
    $interval(function () {
        $scope.theTime = new Date().toLocaleTimeString();
    }, 1000);
});
```

**自定义服务**

```js
//创建名为hexafy 的服务
app.service('hexafy', function() {
    this.myFunc = function (x) {
        return x.toString(16);
    }
});

//要使用自定义服务，需要在定义控制器的时候独立添加，设置依赖关系
app.controller('myCtrl', function($scope, hexafy) {
    $scope.hex = hexafy.myFunc(255);
});

//当你创建了自定义服务，并连接到你的应用上后，你可以在控制器，指令，过滤器或其他服务中使用它
app.filter('myFormat',['hexafy', function(hexafy) {
    return function(x) {
        return hexafy.myFunc(x);
    };
}]);
```

## HTTP

**$http** 是 AngularJS1.5以上 中的一个核心服务，用于读取远程服务器的数据 

```js
// 简单的 GET 请求，可以改为 POST
$http({
    method: 'GET',
    url: '/someUrl'
}).then(function successCallback(response) {
        // 请求成功执行代码
    }, function errorCallback(response) {
        // 请求失败执行代码
});
```

```js
var app = angular.module('myApp', []);
    
app.controller('siteCtrl', function($scope, $http) {
    $http({
        method: 'GET',
        url: 'https://www.runoob.com/try/angularjs/data/sites.php'
    }).then(function successCallback(response) {
            $scope.names = response.data.sites;
        }, function errorCallback(response) {
            // 请求失败执行代码
    });
});
```

```html
<div ng-app="myApp" ng-controller="siteCtrl"> 
    <ul>
        <li ng-repeat="x in names">
            {{ x.Name + ', ' + x.Country }}
        </li>
    </ul>
</div>

<script>
    var app = angular.module('myApp', []);
    app.controller('siteCtrl', function($scope, $http) {
        $http.get("https://www.runoob.com/try/angularjs/data/sites.php")
            .then(function (response) {
            	$scope.names = response.data.sites;
        	}
        );
    });
</script>
```

```js
$http.get('/someUrl', config).then(successCallback, errorCallback);
$http.post('/someUrl', data, config).then(successCallback, errorCallback);
```



## 表格

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
    </head>
    <body>
        
        <div ng-app="myApp" ng-controller="customersCtrl"> 
            <table>
                <tr ng-repeat="x in names">
                    <td>{{ x.Name }}</td>
                    <td>{{ x.Country }}</td>
                </tr>
            </table>
        </div>

        <script>
            var app = angular.module('myApp', []);
            app.controller('customersCtrl', function($scope, $http) {
                $http.get("/try/angularjs/data/Customers_JSON.php")
                    .then(function (result) {
                    $scope.names = result.data.records;
                });
            });
        </script>
    </body>
</html>
```

## 事件

```html
<div ng-app="myApp" ng-controller="personCtrl">
    <button ng-click="toggle()">隐藏/显示</button>
    <p ng-hide="myVar">
        名: <input type="text" ng-model="firstName"><br>
        姓名: <input type="text" ng-model="lastName"><br>
        <br>
        Full Name: {{firstName + " " + lastName}}
    </p>
</div>

<script>
    var app = angular.module('myApp', []);
    app.controller('personCtrl', function($scope) {
        $scope.firstName = "John",
            $scope.lastName = "Doe"
        $scope.myVar = false;
        $scope.toggle = function() {
            $scope.myVar = !$scope.myVar;
        };
    });
</script>
```

## 表单

AngularJS 表单是输入控件的集合，以下 HTML 元素被称为 HTML 控件 

- input 元素

  ```html
  //绑定输入参数到应用中
  
  //单选框
  <form>
      选择一个选项:
      <input type="radio" ng-model="myVar" value="dogs">Dogs
      <input type="radio" ng-model="myVar" value="tuts">Tutorials
      <input type="radio" ng-model="myVar" value="cars">Cars
  </form>
  
  //复选框
  <form>
      Check to show a header:
      <input type="checkbox" ng-model="myVar">
  </form>
  <h1 ng-show="myVar">My Header</h1>
  
  //文本输入
  <form>
      First Name: <input type="text" ng-model="firstname">
  </form>
  <h1>You entered: {{firstname}}</h1>
  
  //将下拉菜单绑定到你的应用中，ng-model属性的值为你在下拉菜单选中的选项，myVar 的值可以是 dogs, tuts, 或 cars
  <form>
  选择一个选项:
  <select ng-model="myVar">
      <option value="">
      <option value="dogs">Dogs
      <option value="tuts">Tutorials
      <option value="cars">Cars
  </select>
  ```

- select 元素

- button 元素

- textarea 元素

## 全局API

AngularJS 全局 API 用于执行常见任务的 JavaScript 函数集合，如：

- 比较对象
- 迭代对象
- 转换对象

全局 API 函数使用 angular 对象进行访问。

| API                                    | 描述                                          |
| -------------------------------------- | --------------------------------------------- |
| `angular.$$lowercase()（angular1.7+）` | 转换字符串为小写                              |
| `angular.$$uppercase()（angular1.7+）` | 转换字符串为大写                              |
| `angular.isString()`                   | 判断给定的对象是否为字符串，如果是返回 true。 |
| `angular.isNumber()`                   | 判断给定的对象是否为数字，如果是返回 true。   |

## 包含

可以在 HTML 中包含 HTML 文件 

```html
<div ng-app="myApp" ng-controller="sitesCtrl"> 
    <div ng-include="'sites.htm'"></div>
</div>

<script>
    var app = angular.module('myApp', []);
    app.controller('sitesCtrl', function($scope, $http) {
        $http.get("sites.php").then(function (response) {
            $scope.names = response.data.records;
        });
    });
</script>

//sites.htm 文件代码
<table>
    <tr ng-repeat="x in names">
        <td>{{ x.Name }}</td>
        <td>{{ x.Url }}</td>
    </tr>
</table>
```

## 依赖注入

```js
var mainApp = angular.module("mainApp", []);

//factory函数来计算或返回值
mainApp.factory('MathService', function() {
    var factory = {};
    factory.multiply = function(a, b) {
        return a * b;
    }
    return factory;
});

//provider 创建一个 service、factory等， get()方法用于返回 value/service/factory
mainApp.config(function($provide) {
    $provide.provider('MathService', function() {
        this.$get = function() {
            var factory = {};
            factory.multiply = function(a, b) {
                return a * b;
            }
            return factory;
        };
    });
});

//value向控制器传递值
mainApp.value("defaultInput", 5);

mainApp.service('CalcService', function(MathService){
    this.square = function(a) {
        return MathService.multiply(a,a);
    }
});

mainApp.controller('CalcController', function($scope, CalcService, defaultInput) {
    $scope.number = defaultInput;
    $scope.result = CalcService.square($scope.number);

    $scope.square = function() {
        $scope.result = CalcService.square($scope.number);
    }
});
```

## 路由

实现多视图的单页 Web 应用（single page web application，SPA）。 单页 Web 应用中 AngularJS 通过 **#! + 标记** 实现 ，Angular1.6 之前的版本是通过 **# + 标记** 实现路由。AngularJS 路由通过 **#! + 标记** 帮助我们区分不同的逻辑页面并将不同的页面绑定到对应的控制器上。 













































