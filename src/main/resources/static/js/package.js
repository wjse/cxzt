(function(){
    $(function(){
        if(isAdmin()){
            showHiddenHtml();
        }
        loadPackage(1);

        $("#search").on("click" , function(){
            $(".form-inline").on("submit" , () => false);
            loadPackage(1);
        });
    });

    const pageSize = 10;
    let invoiceList;

    function loadPackage(pageNum){
        let params = {};

        let user = $("#user").val();
        let startTime = $("#startTime").val();
        if(user){
            params.user = user;
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
            params: {elemId: '#page' , total : page.total , pageIndex: page.pageNum , pageNum : page.pages , pageSize : page.pageSize},
            requestFunction: function () {}
        });

        let list = page.list;
        invoiceList = [];
        if(!list || list.length == 0){
            $("#tbody").html("");
            return;
        }

        let html = "";
        for(let i  = 0 ; i < list.length; i++){
            let invoicePackage = list[i];
            html += renderInvoicePackage(invoicePackage , i);
        }
        $("#tbody").html(html);
        $(".invoiceDetailBtn").on("click" , function(){
            let invoiceId = $(this).attr("invoiceId");
            let invoice;
            for(let i = 0 ; i < invoiceList.length; i++){
                if(invoiceId == invoiceList[i].idString){
                    invoice = invoiceList[i];
                    break;
                }
            }

            if(invoice){
                renderDetail(invoice);
            }
        });

        $("#page").on("click" , "a" , function(){
            console.log(134);
            var flag = $(this).parent().hasClass('disabled');
            if(flag){
                return false;
            }

            var pageIndex = $(this).data('pageindex');
            loadPackage(pageIndex);
        });
    };

    function renderDetail(invoice){
        api("/invoice/detail/" + invoice.idString , "GET" , null , function(res){
            if(res.code == 200){
                $("#invoiceTypeName").html(invoice.invoiceTypeName);
                $("#buyerName").html(invoice.buyerName);
                $("#sellerName").html(invoice.sellerName);
                $("#buyerTaxNo").html(invoice.buyerTaxNo);
                $("#sellerTaxNo").html(invoice.sellerTaxNo);
                $("#buyerAddrTel").html(invoice.buyerAddrTel);
                $("#sellerAddrTel").html(invoice.sellerAddrTel);
                $("#buyerBank").html(invoice.buyerBank);
                $("#sellerBank").html(invoice.sellerBank);
                $("#amount").html(invoice.amount);
                $("#amountTax").html(invoice.amountTax);

                let details = res.data;
                let html = "";
                for(let i = 0 ; i < details.length; i++){
                    let detail = details[i];
                    console.log(detail.unit);
                    html += "<tr>";
                    html += "<td>" + (detail.remark ? detail.remark : '') + "</td>";
                    html += "<td>" + (detail.specs ? detail.specs : '') + "</td>";
                    html += "<td>" + (detail.unit ? detail.unit : '') + "</td>";
                    html += "<td>" + (detail.count ? detail.count : '') + "</td>";
                    html += "<td>" + (detail.price ? detail.price : '') + "</td>";
                    html += "<td>" + (detail.amount ? detail.amount :'') + "</td>";
                    html += "<td>" + (detail.taxRate ? detail.taxRate : '') + "</td>";
                    html += "<td>" + (detail.taxAmount ? detail.taxAmount : '') + "</td>";
                    html += "</tr>";
                }
                $(".invoice-detail-table > tbody").html(html);
                $("#modal").modal("toggle");
            }
        });
    };

    function renderInvoicePackage(invoicePackage , index){
        let html = "<tr class='package'>";
            html += "<th scope='row'>"+ (index + 1) +"</th>";
            html += "<td>" + invoicePackage.user.nickName + "</td>";
            html += "<td>" + new Date(invoicePackage.date).format("yyyy-MM-dd hh:mm:ss") + "</td>";
            html += "<td>" + invoicePackage.count + "</td>";
            html += "<td>" + invoicePackage.amount + "</td>";
            html += "</tr>";
            html += renderInvoice(invoicePackage.id , invoicePackage.invoiceSet);
        return html;
    };

    function renderInvoice(packageId , list){
        let html = "<tr>";
            html += "<td></td>";
            html += "<td colspan='4'>";
            html += "<table class='table'>";
            html += "<thead>";
            html += "<tr>";
            html += "<th>发票号码</th>";
            html += "<th>发票代码</th>";
            html += "<th>开票日期</th>";
            html += "<th>发票类型</th>";
            html += "<th>购买方名称</th>";
            html += "<th>销售方名称</th>";
            html += "<th>金额合计</th>";
            html += "<th>税额合计</th>";
            html += "<th>价税合计</th>";
            html += "<th>&nbsp;</th>";
            html += "</tr>";
            html += "</thead>";
            html += "<tbody>";

            for(let i = 0 ; i < list.length ; i++){
                let invoice = list[i];
                invoiceList.push(invoice);
                html += "<tr>";
                html += "<td>" + invoice.number + "</td>";
                html += "<td>" + invoice.code + "</td>";
                html += "<td>" + new Date(invoice.date).format("yyyy-MM-dd") + "</td>";
                html += "<td>" + invoiceType(invoice.type) + "</td>";
                html += "<td>" + invoice.buyerName + "</td>";
                html += "<td>" + invoice.sellerName + "</td>";
                html += "<td>" + invoice.amount + "</td>";
                html += "<td>" + invoice.taxAmount + "</td>";
                html += "<td>" + invoice.amountTax + "</td>";
                html += "<td><button class='mr-3 btn btn-primary invoiceDetailBtn' invoiceId='" + invoice.idString + "'>发票明细</button></td>";
                html += "</tr>";
            }

            html += "</tbody>";
            html += "</table>";
            html += "</td>";
            html += "</tr>";
        return html;
    };

    function invoiceType(type){
        if(1 == type){
            return "增值税专用发票";
        }else if(2 == type){
            return "增值税普通发票";
        }else if(3 == type){
            return "增值税电子普通发票";
        }else if(4 == type){
            return "机动车销售统一发票";
        }else if(5 == type){
            return "卷式普通发票";
        }else if(6 == type){
            return "电子普通发票[通行费]";
        }else if(7 == type){
            return "二手车统一发票";
        }
    };

    function showHiddenHtml(){
        $("#userGroup").removeClass("hide");
    };

    function isAdmin(){
        return "ADMIN" == window.sessionStorage.getItem("type");
    };
})($);