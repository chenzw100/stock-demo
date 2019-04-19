var xData,yContinueValData,yYesterdayShowData,yNowTemperatureData,yTradeValData;
var myChartContinueVal = echarts.init(document.getElementById('mainContinueVal'));
var myChartNowTemperature = echarts.init(document.getElementById('mainNowTemperature'));



$(function() {

    initView();

});
function initView(){
    getViewData();
    changeView();
}
function getSelect(){
    /*if(!checkSelectTime()){
     return false;
     }*/
    initView();
}
function changeView(){
    drawing();
}

function getViewData(){
    var end = $("#dateEnd").val();
    if(end==undefined || end==""){
        end="1";
    }
    $.ajax({
        url : 'current/'+end,
        dataType : 'json',
        type : 'get',
        async : false,
        success : function(ret){
            var d = ret;
            xData=d["x"];

            yContinueValData=d["yContinueVal"];
            yYesterdayShowData=d["yYesterdayShow"];

            yNowTemperatureData=d["yNowTemperature"];
            yTradeValData=d["yTradeVal"];



        }
    });
}
function drawing(){
    var optionContinueVal = {
        title : {
            text: '连板&昨日'
        },
        tooltip : {
            trigger: 'axis'
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xData
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'连板',
                type:'line',
                smooth:true,
                data:yContinueValData,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            },
            {
                name:'昨日',
                type:'line',
                data:yYesterdayShowData,
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChartContinueVal.setOption(optionContinueVal);
    var optionNowTemperature = {
        title : {
            text: '市场温度'
        },
        tooltip : {
            trigger: 'axis'
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xData
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'市场温度',
                type:'line',
                smooth:true,
                data:yNowTemperatureData,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            }
        ]
    };

    myChartNowTemperature.setOption(optionNowTemperature);
    //=====





}

function checkSelectTime(){
    if($('input:radio[name="item"]:checked').val() == "m") {
        if($("#d5").val()!=""){
            $("#startD").val($("#d5").val()+"-01");
        }else{
            $("#startD").val("");
        }
        if($("#d6").val()==""){
            $("#endD").val("");
        }

    }else{
        $("#startD").val($("#d1").val()),$("#endD").val($("#d2").val())
    }
    if($("#startD").val() == "" || $("#endD").val()=="" ){
        BootstrapDialog.show({
            title: '提示',
            message: '<div style="text-align: center;width:100%">请选择时间段!</div>',
            closable: false,
            size:BootstrapDialog.SIZE_SMALL,
            buttons: [{
                label: '确定',
                cssClass: 'btn-main2',
                action: function(dialogRef){
                    dialogRef.close();
                }
            }]
        }).getModalHeader().css('background-color', '#333');

        return false;
    }
    return true;
}







