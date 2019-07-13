var app = new Vue({
    el:"#app",
    data:{
        //列表
        entityList:[],
        total:0,
        pageNum:1,
        pageSize:10,
        entity:{},
        ids:[],
        searchEntity:{},
        check:false
    },
    methods: {
        selectAll:function () {
                for ( i=0;i<app.entityList.length;i++){
                    app.ids.push(app.entityList[i].id);
                }
                if (app.check){
                    app.ids = [];
                }

        },

        searchList: function (pageNum) {
            this.pageNum = pageNum;
            axios.post("../brand/search.do?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize, this.searchEntity).then(function (response) {
                app.entityList = response.data.list;
                app.total = response.data.total;
                app.searchEntity = {};
            })
        },

        deleteList: function () {
            if (this.ids.length == 0) {
                alert("请先选择要删除的列")
            } else {
                if (confirm("确定要删除吗？")) {
                    axios.get("../brand/deleteList.do?ids=" + this.ids).then(function (response) {
                        if (response.data.success) {
                            app.searchList(1);
                            app.entity = {};
                            app.ids = [];
                            app.check=false;
                        } else {
                            alert(response.data.message);
                        }
                    });
                }
            }

        },

        save: function () {
            if (this.entity.id != null) {
                var method = "update";
            } else {
                method = "add";
            }
            axios.post("../brand/" + method + ".do", this.entity).then(function (response) {
                if (response.data.success) {
                    app.searchList(app.pageNum);
                    app.entity = {};
                } else {
                    alert(response.data.message);
                }
            });
        },

        one: function (id) {
            axios.get("../brand/findOne/" + id + ".do").then(function (response) {
                app.entity = response.data;

            })
        },

        /*         	    searchList:function (pageNum) {
                     this.pageNum = pageNum;
                     axios.get("../brand/findPage.do?pageNum="+this.pageNum+"&pageSize="+this.pageSize).then(function (response) {
                         app.entityList = response.data.list;
                         app.total = response.data.total;
                     }).catch(function () {
                         alert("请求失败！！！！！！")
                     });
                 }*/
    },
    created() {
        /* axios.get("../brand/findAll.do").then(function (response) {
        console.log(response);
        app.entityList = response.data;
    }).catch(function () {
        //网络中断
        alert("获取数据失败！！");
    });*/
        //刚进来调用
        this.searchList(this.pageNum);

    }

});