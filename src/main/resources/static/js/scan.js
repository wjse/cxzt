(function(){
    let invoiceList = [];
    $(function(){
        $("#scanner").val(window.sessionStorage.getItem("nickName"));
        getScan();
    });


    function getScan(){
        let code = "";
        document.onkeypress = function(event){
            if(event.which != 13){
                if(event.key){
                    code += event.key;
                }
            }else{
                getInfo(code);
                code = "";
                flush();
            }
        };
    };

    function flush(){
        if(!invoiceList || invoiceList.length == 0){
            return;
        }

        let html = "";
        let totalAmount = 0;
        let count = 0;
        for(let i = 0 ; i < invoiceList.length; i++){
            let invoice = invoiceList[i];
            html += "<tr>";
            html += "<th scope='row'>"+ (i+1) +"</th>";
            html += "<td>" + invoice.code + "</td>";
            html += "<td>" + invoice.number + "</td>";
            html += "<td>" + invoice.amount + "</td>";
            html += "<td>" + invoice.dateString + "</td>";
            html += "<td>" + invoice.check + "</td>";
            html += "</tr>";
            totalAmount += parseFloat(invoice.amount);
            count++;
        }

        $("#tbody").html(html);
        $("#totalAmount").val(totalAmount);
        $("#count").val(count);
    };

    function getInfo(code){
        if(!code){
            return;
        }
        let array = code.split(",");
        let invoice = {
            typeString : getInvoiceType(array[1]),
            type : array[1],
            code : array[2],
            number : array[3],
            amount : array[4],
            date : getDate(array[5]).getTime(),
            dateString : getDateString(array[5]),
            check : array[6]
        };

        let has = false;
        for(let i in invoiceList){
            let _invoice = invoiceList[i];
            if(_invoice.code == invoice.code && _invoice.number == invoice.number){
                has = true;
                break;
            }
        }

        if(!has){
            invoiceList.push(invoice);
        }
    };

    function getInvoiceType(type){
        if("10" == type){
            return "增值税电子普通发票";
        }else if("04" == type){
            return "增值税普通发票";
        }else if("01" == type){
            return "增值税专用发票";
        }else{
            return "未知类型";
        }
    };

    function getDateString(str){
        let yyyy = str.substring(0 , 4);
        let MM = str.substring(4 , 6);
        let dd = str.substring(6 , 8);
        return yyyy + "-" + MM + "-" + dd;
    };

    function getDate(str){
        let yyyy = str.substring(0 , 4);
        let MM = str.substring(4 , 6);
        let dd = str.substring(6 , 8);
        return new Date(yyyy + " " + MM + " " + dd);
    };
})($);