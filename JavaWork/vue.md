# Vue

https://www.zhihu.com/question/67685381

## 概述

### 环境

NodeJs是一个JavaScript运行环境，是一个平台，类比于JVM

Npm是依赖包管理工具，Webpack是一个前端资源加载/打包工具，Npm和WebPack类比于maven

cnpm淘宝镜像

Vue-cli是Vue命令行工具，可以快速创建集成Vue前端项目，内置webpack

### 概念

- 提供**MVVM**双向数据绑定，依赖**模板表达式和计算属性**
- 通过**指令**将数据和页面进行交互
- 对页面的各个模块进行拆分，变成单独的**组件**，然后通过**数据绑定**，调用对应的模板组件，同时传入参数
- 通过**客户端路由**实现页面的平滑切换

**MVVM**

view -- ViewModel -- Model

DOM--         Vue      -- javaScript对象数据

**数据驱动**

传统使用js操作dom改变视图，vue改变数据自动更新视图变化。

**组件化**

把整一个网页拆分成一个个组件（区块），每个区块作成一个组件，网页由多个组件(如菜单，主体内容...)拼接或嵌套而成，组件可以复用。通过路由的不同加载不同的组件(不同的主体内容...)到主页面中，如：`https://7.220.28.236:9011/console/index.html#/knowledgeCenter`或`https://7.220.28.236:9011/console/index.html#/CampaignPage`。

**SPA (Single-page application) 单页面应用**：只有一张Web页面应用，是加载单个HTML页面并在用户与应用程序交互时动态更新该页面的web应用程序。在URL中采用#号来作为当前视图的地址，改变#号后的参数来载入不同的页面片段。



app.js是程序的入口模块，所有请求进入这里进行处理，在通过router.js进行路由分发







### 项目目录

- **node_modules**：项目依赖
- **public**：Vue3.x的静态资源目录，相当于vue2.x的static，目录下的文件会不做处理直接被复制到最终打包目录，必须使用绝对路径引用。
- **src**：源码目录
  - **assets**：静态资源目录，会被webpack处理为模块依赖，只能用相对路径
  - **components**：vue组件
  - **App.vue**：默认入口组件，它的vm实例控制的是index.html中的div区域
  - **main.js**：项目入口文件，webpack编译打包从此文件开始，所有用到的资源应该从此import引入，最终打包的js文件会自动插入到index.html页面中
  - **babel.config.js**：Babel配置文件，Babel是处理es6高级语法转换库，会将es6的高级语法转换为低级浏览器可以识别的js代码。
  - **package-lock.json**：记录当前项目依赖的所有库的具体来源和版本号
  - **package.json**：项目说明文件以及依赖包列表
    - **script**：项目运行脚本
      - **serve**：输入`npm run serve`会执行当前serve配置值
      - **build**：输入`npm run build`会执行当前build配置值
    - **dependencies和devDependencies**：项目依赖列表，其中dependencies是开发生产环境都需要的依赖，devDependencies是开发依赖，webpack打包不会打包进去
    - **eslintConfig**：js代码检测工具

### 启动流程

1. 输入`npm run serve`，到package.json查找script中serve配置运行命令
2. 加载依赖node_modules
3. 把其他组件导入到App.vue中，调用render()方法渲染组件；解析main.js之后生成打包之后的文件app.js，植入到index.html中；构建页面成功





### 样例代码

#### 整合

```html
<html>
    <head>
        ...
    </head>
    <body>
        <div id="app">
            {{msg}}
        </div>
        <script>
            var vm = new Vue({
                el: "#app",		// vue实例要控制的区域
                data: {
                    msg:"hello vue"
                }
            });
        </script>
    </body>
</html>
```

#### 项目

##### App.vue

```vue
// 组件模板，对应要展示的界面
<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        name: "app"
    };
</script>
```

##### main.js

```javascript
import Vue from "vue";
import App from "./App";
import router from "./router";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import Print from "@/vendor/print";
import $ from "jquery";
import "babel-polyfill";
import "@/api/js/self_adaption.js";
import axios from "@/utils/axios.js";
import Echarts from "echarts";
import tools from "@/utils/tools.js";
import apis from "@/utils/apis.js";
import validation from "@/utils/validation.js";
import "./assets/css/index.css";

Vue.use(ElementUI);
Vue.use(Print);
Vue.use(tools);
Vue.use(axios);
Vue.use(apis);
Vue.use(validation);
Vue.config.productionTip = false;
Vue.prototype.$echart = Echarts;

var vm = new Vue({
    el: "#app",		
    router,
    template: "<App/>",
    components: {
        App
    }
});
```

##### index.html

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>vue</title>
        <style>
            html,
            body {
                margin: 0;
                padding: 0;
                background: #fff;
                font-size: 14px;
            }

            [v-cloak] {
                display: none;
            }
        </style>
    </head>

    <body>
        <div id="app" v-clock></div>
    </body>
</html>
```

##### route.js

```js
import Vue from "vue";
import Router from "vue-router";
import knowledgeCenter from "@/pages/knowledge-center/main-layout";
import MainIndex from "@/pages/main-index.vue";

Vue.use(Router);
export default new Router({
  routes: [
    {
      name: "knowledgeCenter",
      path: "/knowledgeCenter",
      meta: {
        requireAuth: true
      },
      component: knowledgeCenter
    },
    {
      name: "MainIndex",
      path: "/MainIndex",
      meta: {
        requireAuth: true
      },
      component: MainIndex
    }
  ]
});
```

##### main-index.vue

```vue
<style scoped>
    .content {
        width: 100%;
        background: #e2e8f4;
        height: 500px;
        display: flex;
        align-items: center;
    }
......
</style>

<template>
<div>
    <div class="content">
      <div class="box">
        <div class="item">
            
          <div class="cup" @click="downloadDocument('one')">
            <div class="item-icon">
              <img src="../assets/imgs/main-index/index_icon-1.png" alt="" />
            </div>
            <div class="item-title">入门指导</div>
          </div>
            
          <div
            class="item-desc"
            v-for="(x, i) in settings.gettingStarted.split('<br>')"
            :key="i"
          >
            {{ x }}
          </div>
            
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Bot from "../components/bottom.vue";
export default {
    components: { Bot },
    data() {
        return {
            settings: {
                functionDescription: "",
                gettingStarted: "",
                heartSupport: ""
            }
        };
    },
    created() {
        this.getSettings();
    },
    methods: {
        getSettings() {
            this.$apis.getHeartSupport().then(res => {
                this.settings = res.data;
            });
        },
        downloadDocument(type) {
            this.$apis.downloadDocument({ name: type }).then(res => {
                this.$tools.creatAElement(res.data.resultData, res.data.fileName);
            });
        }
    }
};
</script>
```

## 语法

### 插值表达式

```html
<body>
    <div id="app">
        <!-- 模板表达式，对应vm实例上的data对象中的msg属性，只要定义在vm实例上的data对象中的msg属性发生了变化，插值处的内容会自动更新 -->
        {{msg}}
    </div>
</body>
```



### 指令

#### v-cloak

解决闪烁问题

```html
<style>
    [v-cloak]{
        display:none;
    }
</style>

<body>
    <div id="app">
        <p v-cloak>{{msg}}</p>
    </div>
</body>
```



#### v-text和v-html

设置标签的文本内容，text纯文本设置，html设置html文本

```html
index.html
<html>
    <body>
        <div id="app">
            <p v-text="msg2"></p>
            <p v-html="msg2"></p>
        </div>
    </body>
</html>
```

```javascript
main.js
var vm = new Vue({
    el:"#app",
    data:{
        msg2:"<h1>我是一个H1标题，让我变大</h1>"
    }
});
```

#### v-bind和v-on

v-bind属性绑定，绑定的数据在vm实例的data属性中声明

v-on事件绑定，绑定的数据在vm实例的methods属性中声明

```vue
<input type="button" value="按钮" v-bind:title="mytitle"/>
<input type="button" value="按钮" :title="mytitle+'qwe123'"/>

export default {
  data() {
    return {
      mytitle: "云班100"
    };
  }
};
```

```vue
<!-- 通过v-bind:style属性绑定设置样式 -->
<h1 :style="{color:'red','font-size':'40px'}">这是一个H1</h1>

<h1 :style="[styleObj1,styleObj2]">这是一个H1</h1>
data:{
	styleObj1:{color:'red','font-size':200},
	styleObj2:{'font-style':'italic'}
}
```



```vue
<input type="button" value="按钮" v-on:click="show('xx')"/>
<input type="button" value="按钮" @click="show"/>

export default {
  methods: {
    show(val) {
      ...
    }
  }
};
```



#### 事件修饰符

@事件名:事件修饰符="xx"

- **.stop**：阻止事件冒泡
- **.prevent**：阻止默认行为
- **.capture**：
- **.self**
- **.once**

#### v-model

双向数据绑定

v-bind和{{xx}}插值表达式只能实现数据的单向绑定，从M自动绑定到V

v-model只能运用在表单元素中，v变化时M也自动更新

```html
<div id="app">
    姓名：<input type="text" style="width:100%;" v-model="msg" @keyup="showMsg"/>
</div>
```

```vue
var vm=new Vue({
    ..
    data:{msg:""},
    methods:{
		showMsg(){console.log(this.msg)}
    }
    ..
})
```

#### v-for和key

```vue
<!-- 遍历普通数组 -->
<p v-for="(item,index) in list">索引：{{index}}---每一项:{{item}}</p>

<!-- 遍历对象数组 -->
<p v-for="(item,index) in listObj">索引：{{index}}---每一项id:{{item.id}}:每一项name"{{item.name}}</p>

<!-- 遍历对象每一个属性值 -->
<p v-for="(val,key,i) in user">{{key}}:{{val}}--索引：{{i}}</p>

<!-- 遍历数字 -->
<p v-for="count in 10">这是第{{count}}次循环</p>

<!-- key属性，只能使用number或string，同时确保key的属性值唯一 -->
<p v-for="item in list" :key="item.id">
    <input type="checkbox">{{item.id}} --- {{item.name}}
</p>
```

#### v-if和v-show

控制元素的显示和隐藏

- v-if：每次都会重新删除或创建元素
- v-show：每次不会重新进行DOM的创建和删除操作，只是切换了元素的 display:none 样式

```html
<body>
    <div id="app">
        <input type="button" value="toggle" @click="toggle">
        <h3 v-if="flag">...</h3>
        <h3 v-show="flag">...</h3>
    </div>
</body>
```

```vue
data:{
	flag:true
},
method:{
	toggle(){this.flag=!this.flag}
}
```

### 过滤器

用在插值表达式或v-bind表达式中，过滤器添加在JS表达式尾部，由“管道符”指示，用于实现文本格式化等

> {{xxx | 过滤器}} 或 <p: v-bind="title | 过滤器"></p>

**私有过滤器**

只能在绑定的vue实例所控制的区域中使用

```vue
<!-- 和vm实例中的data、methods属性一样 -->
filter:{
<!-- data:要过滤的数据，即管道符|前面的数据，后面是参数列表 -->
	dateFormat:function(data,pattern=""){
        ...
        return "xxx"
    }
}
```

**全局过滤器**

所有vm实例共享

```vue
Vue.filter('dateFormat',function(data,pattern=""){
	var dt = new Date(data);
	...
    if(pattern.toLowerCase()==='yyyy-mm-dd'){
    	return `${y}-${m}-${d}`
    }else{
    	return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
    }
})
```



**调用过滤器**

```vue
<td>{{item.ctime | dateFormat("yyyy-mm-dd hh:mm:ss")}}</td>
```

局部过滤器优先于全局过滤器被调用

### 键盘修饰符

```vue
<label>
	Name:
    <!-- keyup监听键盘抬起事件，enter表示监听enter键 -->
    <input type="text" class="form-control" v-model="name" @keyup.enter="add()">
</label>
```

**键盘的常用按键别名**
.enter .tab .delete .esc .space .up .down .left .right

**自定义键盘**

按键不在上述别名中，使用按键对应的keycode或者自定义

```vue
<!-- 113对应键盘F2 -->
<input type="text" class="form-control" v-model="name" @keyup.113="add()">

<!-- 自定义键盘F2 -->
Vue.config.keyCodes.f2=113
<input type="text" class="form-control" v-model="name" @keyup.f2="add()">
```

### 组件

#### 组件传值

- 父组件向子组件传值：和属性绑定不同，父组件通过 `v-bind:属性名='属性值'` 的方式发送数据到子组件，子组件通过 `props` 属性接收来自父组件的数据
- 子组件向父组件传值：父组件通过 `v-on:事件名称='方法名(传递参数)'` 绑定一个方法，子组件定义函数，在函数内调用 `this.$emit('事件名称', '传递参数')` 触发父组件事件绑定的方法 `方法名` ，把 `传递参数` 作为事件方法的参数传递

**父组件向子组件传值**

```vue
<!-- 父组件 -->
<template>
	<div>
        <transition mode="out-in">
            <component v-bind:isDisabled="comName" v-bind:msg="toSonMsg"></component>
    	</transition>
    </div>
</template>
<script>
export default {
  data() {
    return {
      comName: true
    };
  }
};
</script>

<!-- 子组件 -->
<template>
	<div>
        <h3>{{msg}}</h3>
    </div>
</template>
<script>
export default {
  props:["msg"]
  /* props: {
    submitForm: Object,
    msg: String,
    isDisabled: Boolean,
    isAdd: Boolean
  } */
};
</script>
```





## Other

[vue中的两个方法同步执行](https://blog.csdn.net/weixin_42028153/article/details/124836262)

[VUE 生命周期函数](https://blog.csdn.net/qq_46179813/article/details/106909224)

[vue中异步函数async和await](https://blog.csdn.net/qq_25135655/article/details/114967749)

### console.log()

```html
打印对象

<!-- 对象res隐式转换成字符串，得到的是res[object Object] -->
console.log('res'+res)

<!-- 打印出来的是对象及其属性 -->
console.log('res', res)

<!-- 打印出来的是JSON字符串 -->
console.log(JSON.stringify(res))

<!-- 打印出来的是JSON对象 -->
console.log(JSON.parse(JSON.stringify(res)))
```































