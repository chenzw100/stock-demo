var xData,yContinueValData,yYesterdayShowData,yNowTemperatureData,yTradeValData,yContinueCountData,yDownCountData,yUpData,yDownData,yAverageTodayOpenData,yAverageTodayCloseData,yAverageTomorrowOpenData,yAverageTomorrowCloseData,hotData;
var yLimitUpCountData,yLimitUpCountData,yBrokenData,yFirstContinueData,ySecondContinueData;
var myChartContinueVal = echarts.init(document.getElementById('mainContinueVal'));
var myChartNowTemperature = echarts.init(document.getElementById('mainNowTemperature'));
var myChartTradeVal = echarts.init(document.getElementById('mainTradeVal'));
var myChartStrongCount = echarts.init(document.getElementById('mainStrongCount'));
var myChartCount = echarts.init(document.getElementById('mainCount'));
var myChartCount2 = echarts.init(document.getElementById('mainCount2'));
var myChartLimitDownCount= echarts.init(document.getElementById('mainLimitDown'));
var myChartLimitUpCount= echarts.init(document.getElementById('mainLimitUp'));
var myChartBroken= echarts.init(document.getElementById('mainBroken'));

var myChartAverageTodayOpen = echarts.init(document.getElementById('mainAverageTodayOpen'));
var myChartAverageTodayClose = echarts.init(document.getElementById('mainAverageTodayClose'));
var myChartAverageTomorrowOpen = echarts.init(document.getElementById('mainAverageTomorrowOpen'));
var myChartAverageTomorrowClose = echarts.init(document.getElementById('mainAverageTomorrowClose'));

var myChartSpaceHeight = echarts.init(document.getElementById('mainSpaceHeight'));
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
            yLimitUpCountData=d["yLimitUp"];
            yLimitDownCountData=d["yLimitDown"];
            yBrokenData=d["yBroken"];

            yAverageTodayOpenData=d["yAverageTodayOpen"];
            yAverageTodayCloseData=d["yAverageTodayClose"];
            yAverageTomorrowOpenData=d["yAverageTomorrowOpen"];
            yAverageTomorrowCloseData=d["yAverageTomorrowClose"];

            yFirstContinueData=d["firstContinue"];
            ySecondContinueData=d["secondContinue"];

            hotData=d["hot"];
        }
    });
}
function drawing(){

    var optionSpaceHeight = {
        /*title : {
         text: '连板&昨日'
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
            },
            {
                name:'次高',
                type:'line',
                data:ySecondContinueData,
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
    myChartSpaceHeight.setOption(optionSpaceHeight);


    var optionContinueVal = {
        /*title : {
            text: '连板&昨日'
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

    var optionTradeVal = {
        /*title : {
            text: '量'
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
        /*title : {
            text: '连板&涨停'
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
            },
            {
                name:'涨停数',
                type:'line',
                data:yUpData,
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
    myChartCount.setOption(optionCount);

    var optionCount2 = {
        /*title : {
            text: '跌停'
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
                name:'跌停',
                type:'line',
                data:yDownData,
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
    myChartCount2.setOption(optionCount2);
    //----------核按钮-------
    var optionAverageTodayOpen = {
        /*title : {
         text: '跌停'
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
                name:'今日竞价',
                type:'line',
                data:yAverageTodayOpenData,
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
    myChartAverageTodayOpen.setOption(optionAverageTodayOpen);
    var optionAverageTodayClose = {
        /*title : {
         text: '跌停'
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
                name:'今日收盘',
                type:'line',
                data:yAverageTodayCloseData,
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
    myChartAverageTodayClose.setOption(optionAverageTodayClose);
    var optionAverageTomorrowOpen = {
        /*title : {
         text: '跌停'
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
                name:'明日开盘',
                type:'line',
                data:yAverageTomorrowOpenData,
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
    myChartAverageTomorrowOpen.setOption(optionAverageTomorrowOpen);
    var optionAverageTomorrowClose = {
        /*title : {
         text: '跌停'
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
                name:'明日收盘',
                type:'line',
                data:yAverageTomorrowCloseData,
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
    myChartAverageTomorrowClose.setOption(optionAverageTomorrowClose);

    var optionLimitDownCount = {
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
    hotDataShow();

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







