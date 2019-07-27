var app = new Vue({
    el:"#app",
    data:{
        resultMap:{"itemList":[]},
        searchMap:{"keywords":"","brand":"","category":"","price":"","spec":{},"pageNo":1,"pageSize":20,"sortField":"","sort":""},
        pageNoList:[],
        frontDot:false,
        backDot:false
     },
    methods:{
        //根据参数名字获取参数
        getParameterByName: function (name) {
            return decodeURIComponent((new RegExp('[?|&]' + name + '=' +
                '([^&;]+?)(&|#|;|$)').exec(location.href) || [,
                ""])[1].replace(/\+/g, '%20')) || null
        },

        sortSearch:function (sortField,sort) {
            this.searchMap.sort = sort;
            this.searchMap.sortField = sortField;

            this.searchMap.pageNo = 1;
            this.search();
        },

        //根据页号查询
        queryByPageNo: function(pageNo){
            if(1<= pageNo && pageNo<=this.resultMap.totalPages){
                this.searchMap.pageNo = pageNo;
                this.search();
            }
        },

        buildPagination:function () {
            this.pageNoList = [];
            //起始页号
            var startPageNo = 1;
            //结束页号
            var endPageNo = this.resultMap.totalPages;
            //要显示的总页号数
            var showPageNoTotal = 5;

            //总页数大于 要显示的总页号数
            if (this.resultMap.totalPages > showPageNoTotal) {
                //当前页左右间隔
                var interval = Math.floor(showPageNoTotal/2);
                startPageNo = this.searchMap.pageNo - interval;
                endPageNo = this.searchMap.pageNo + interval;

                if(startPageNo > 0){
                    if (endPageNo > this.resultMap.totalPages) {
                        endPageNo = this.resultMap.totalPages;
                        startPageNo = endPageNo - showPageNoTotal + 1;
                    }
                } else {
                    //起始页必须要从1开始
                    startPageNo = 1;
                    endPageNo = showPageNoTotal;
                }
            }

                this.frontDot=false;
                this.backDot=false;

            if (startPageNo>1){
                this.frontDot = true;
            }
            if (endPageNo<this.resultMap.totalPages){
                this.backDot = true;
            }

            for (var i = startPageNo; i <= endPageNo; i++) {
                this.pageNoList.push(i);
            }
        },

        search:function () {
            axios.post("itemSearch/search.do",
                this.searchMap).then(function (response) {
                app.resultMap = response.data;
                //构建分页导航条方法
                app.buildPagination();
            });
        },
        addSearchItem:function (key,value) {
            if (key=='brand' || key=='category' || key=='price'){
                app.searchMap[key] = value;
            }else {
                this.$set(app.searchMap.spec, key, value);
            }
            //重置搜索页数
            this.searchMap.pageNo=1;
            this.search();
        },
        removeSearchItem:function (key) {
            if (key=='brand' || key=='category'||key=='price'){
                app.searchMap[key] = "";
            }else {
                this.$set(app.searchMap.spec,key,null);
                delete this.searchMap.spec[key];
            }
            //重置搜索页数
            this.searchMap.pageNo=1;
            this.search();
        }
    },
    created(){
        //获取浏览器地址的搜索关键字
        var keywords = this.getParameterByName("keywords");
        if (keywords!=null){
            this.searchMap.keywords = keywords;
        }
        this.search();
    }
});