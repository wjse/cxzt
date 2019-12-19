(function(){
    $('#export').popover({
        title : "请选择导出发票的时间段",
        content : popoverHtml,
        html : true
    });

    function popoverHtml(){
        let html = "<div class='form-inline' id='popover'>";
        html += "<div class='form-group'>";
        html += "<h5 for='startDate' style='margin-right:15px;'>开始日期</h5>";
        html += "<input id='startDate' type='date' class='mr-3 form-control'>";
        html == "</div>";
        html += "<div class='form-group'>";
        html += "<h5 for='endDate' style='margin-right:15px;'>结束日期</h5>";
        html += "<input id='endDate' type='date' class='mr-3 form-control'>";
        html += "</div>";
        html += "<div class='form-group'>";
        html += "<button id='exportSubmit' class='btn btn-primary'>确定</button>";
        html += "</div>";
        html += "</div>";
        return html;
    };

    $("#export").on("shown.bs.popover" , function(){
        $("#exportSubmit").on("click" , function(){
            let sd = $("#startDate").val() , ed = $("#endDate").val();
            let url = "/export?token=" + window.sessionStorage.getItem("token");

            if(sd){
                url += "&startTime=" + new Date(sd).getTime();
            }

            if(ed){
                url += "&endTime" + new Date(ed).getTime();
            }

            window.location.href = url;
        });
    });
})($);