var app = new Vue({
    el:"#app",
    data:{
        resultMap:{"itemList":[]},
        searchMap:{"keyword":""}
    },
    methods:{
        search:function () {
            axios.post("itemSearch/search.do",
                this.searchMap).then(function (response) {
                app.resultMap = response.data;
            })
        }
    },
    created(){
        this.search();
    }
});