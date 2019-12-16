(function(){
    $(function(){
        if(isAdmin()){
            showHiddenHtml();
        }
        loadPackage(1);

        $("#search").on("click" , function(){
            $(".form-inline").on("submit" , () => false);
            loadPackage(1);//TODO
        });
    });

    const pageSize = 10;

    function loadPackage(pageNum){
        let params = {};

        let userId = $("#user").val();
        let startTime = $("#startTime").val();
        if(userId){
            params.userId = userId;
        }

        if(startTime){
            params.startTime = new Date(startTime).getTime();
        }

        api("/invoice/package?pageNum=" + pageNum + "&pageSize=" + pageSize , "GET" , params , function(res){
            if(res && res.code == 200){
                render(res.data);
            }
        });
    };

    function render(page){
        P.initMathod({
            params: {elemId: '#page' , total : page.total , pageIndex: page.pages , pageNum : page.pageNum , pageSize : page.pageSize},
            requestFunction: function () {}
        });

        let list = page.list;
        if(!list || list.length == 0){
            return;
        }

        let html = "";
        for(let i  = 0 ; i < list.length; i++){
            let invoicePackage = list[i];
            html += renderInvoicePackage(invoicePackage , i);
        }
        $("#tbody").html(html);
    };

    function renderInvoicePackage(invoicePackage , index){
        let html = "<tr>";
            html += "<th scope='row'>"+ (index + 1) +"</th>";
            html += "<td>" + invoicePackage.user.nickName + "</td>";
            html += "<td>" + new Date(invoicePackage.date).format("yyyy-MM-dd hh:mm:ss") + "</td>";
            html += "<td>" + invoicePackage.count + "</td>";
            html += "<td>" + invoicePackage.amount + "</td>";
            html += "</tr>";
        return html;
    };

    function showHiddenHtml(){
        $("#userGroup").removeClass("hide");
    };

    function isAdmin(){
        return "ADMIN" == window.sessionStorage.getItem("type");
    };
})($);