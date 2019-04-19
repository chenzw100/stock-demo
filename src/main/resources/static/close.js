var xData,yContinueValData,yYesterdayShowData,yNowTemperatureData,yTradeValData,yContinueCountData,yDownCountData,yUpData,yDownData;
var myChartContinueVal = echarts.init(document.getElementById('mainContinueVal'));
var myChartNowTemperature = echarts.init(document.getElementById('mainNowTemperature'));
var myChartTradeVal = echarts.init(document.getElementById('mainTradeVal'));
var myChartStrongCount = echarts.init(document.getElementById('mainStrongCount'));
var myChartCount = echarts.init(document.getElementById('mainCount'));

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
        url : 'close/'+end,
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

            yContinueCountData=d["yContinueCount"];
            yDownCountData=d["yDownCount"];

            yUpData=d["yUp"];
            yDownData=d["yDown"];
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

    var optionTradeVal = {
        title : {
            text: '量'
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
                name:'量',
                type:'line',
                smooth:true,
                data:yTradeValData,
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

    myChartTradeVal.setOption(optionTradeVal);

    var optionStrongCount = {
        title : {
            text: '连板数&亏损数'
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
                name:'连板数',
                type:'line',
                smooth:true,
                data:yContinueCountData,
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
                name:'亏损数',
                type:'line',
                data:yDownCountData,
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
    myChartStrongCount.setOption(optionStrongCount);

    var optionCount = {
        title : {
            text: '涨停&跌停'
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
                name:'涨停',
                type:'line',
                smooth:true,
                data:yUpData,
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
                name:'跌停',
                type:'line',
                data:yDownData,
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
    myChartCount.setOption(optionCount);

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







