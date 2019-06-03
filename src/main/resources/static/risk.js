var xData,yContinueValData,yNowTemperatureData,yContinueCountData,yDownCountData,hotData;
var yLimitUpCountData,yLimitUpCountData,yBrokenData,yFirstContinueData;
var myChartNowTemperature = echarts.init(document.getElementById('mainNowTemperature'));
var myChartContinueVal = echarts.init(document.getElementById('mainContinueVal'));
var myChartSpaceHeight = echarts.init(document.getElementById('mainSpaceHeight'));

var myChartStrongCount = echarts.init(document.getElementById('mainStrongCount'));
var myChartLimitUpCount= echarts.init(document.getElementById('mainLimitUp'));

var myChartDownCount = echarts.init(document.getElementById('mainDownCount'));
var myChartLimitDownCount= echarts.init(document.getElementById('mainLimitDown'));


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
        url : 'risk/'+end,
        dataType : 'json',
        type : 'get',
        async : false,
        success : function(ret){
            var d = ret;
            xData=d["x"];

            yNowTemperatureData=d["yNowTemperature"];
            yFirstContinueData=d["firstContinue"];

            yDownCountData=d["yDownCount"];
            yLimitDownCountData=d["yLimitDown"];

            yContinueCountData=d["yContinueCount"];
            yLimitUpCountData=d["yLimitUp"];
            yContinueValData=d["yContinueVal"];


        }
    });
}
function drawing(){

    var optionContinueVal = {
        /*title : {
         text: '连板表现'
         },*/
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
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChartContinueVal.setOption(optionContinueVal);
    var optionSpaceHeight = {
        /*title : {
         text: '空间高度'
         },*/
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
                name:'最高',
                type:'line',
                smooth:true,
                data:yFirstContinueData,
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
    // 使用刚指定的配置项和数据显示图表。
    myChartSpaceHeight.setOption(optionSpaceHeight);

    var optionNowTemperature = {
        /*title : {
            text: '市场温度'
        },*/
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
    var optionStrongCount = {
        /*title : {
            text: '连板数'
        },*/
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
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChartStrongCount.setOption(optionStrongCount);

    var optionCount = {
        /*title : {
            text: '核按钮'
        },*/
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
                name:'核按钮',
                type:'line',
                smooth:true,
                data:yDownCountData,
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
    // 使用刚指定的配置项和数据显示图表。
    myChartDownCount.setOption(optionCount);




    var optionLimitDownCount = {
        /*title : {
         text: '跌停数'
         },*/
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
                name:'跌停数',
                type:'line',
                smooth:true,
                data:yLimitDownCountData,
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
    myChartLimitDownCount.setOption(optionLimitDownCount);
    var optionLimitUpCount = {
        /*title : {
         text: '涨停数'
         },*/
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
                name:'涨停数',
                type:'line',
                smooth:true,
                data:yLimitUpCountData,
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
    myChartLimitUpCount.setOption(optionLimitUpCount);

    var optionBroken = {
        /*title : {
         text: '炸板率'
         },*/
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
                name:'炸板率',
                type:'line',
                smooth:true,
                data:yBrokenData,
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
    myChartBroken.setOption(optionBroken);
    //hotDataShow();

}
function hotDataShow() {
    $("#mainHot").empty();
    for (var i=0;i<hotData.length;i++)
    {
        $("#mainHot").append(hotData[i].name+"【竞价:" +hotData[i].todayOpenRate+"["+hotData[i].remark+"],明天开:"+hotData[i].tomorrowOpenRate+ ",明天收:"+hotData[i].tomorrowCloseRate+":"+hotData[i].plateName+"】<br>");
    }

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







