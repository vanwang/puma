(function(){
	DaylogModule = {
			contenturl : "daylog.html",
			closable:true,
			init:init,
			onclose:onClose
	};
	
	function onClose(){
		console.log("close daylog-module");
	}
	
	function init(){
        $('#slider').sliderNav();
        
        initCalendar();
        
        initPreviewPanel();
	}
	
	function initPreviewPanel(){
		// preview pane setup
	    $('.eventviewlist > li').live("click",function(){
	        var url = $(this).find('a.more').attr('href');
	        if (!$(this).hasClass('current')) {
	            $('.hips-container .preview').animate({left: "-375px"}, 300, function(){
	                $(this).animate({left: "-12px"}, 500).html('<img src="resources/themes/img/loading.gif" />');
	                $preview = $(this);
	                $.ajax( {
	    	            "dataType": 'HTML',
	    	            "type": "POST",
	    	            "url": "test.html",
	    	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	    	            "success": function(data){
	    	            	$preview.html(data);
	    	            },
	    	            "error":function(data){
	    				}
	    	          } );
	            });
	        } else {
	            $('.hips-container .preview').animate({left: "-375px"}, 300);
	        }
	        $(this).toggleClass('current').siblings().removeClass('current');
	        return false;
	    });
	    
	    $('.eventviewlist > li a:not(.more)').live("click",function(e){ e.stopPropagation(); });
	    
	    $('#friend-list-show-btn-id').live('click', function(){
	    	console.log($('#friend-list-id').width());
	    	if($('#friend-list-id').width() > 0){
	    		$('#friend-list-id').animate({width: "0px"}, 300);
	    	}else{
	    		$('#friend-list-id').animate({width: "250px"}, 300);
	    	}
	        return false;
	    });
	    
	    $('.hips-container .preview .close').live('click', function(){
	        $('.hips-container .preview').animate({left: "-375px"}, 300);
	        $('.list-view li').removeClass('current');
	        return false;
	    });
	    // preview pane setup end

	    // floating menu and preview pane
	    $("#gridcontainer").scroll(function () {
	        var offset = 0;
	        if ($('.hips-container .preview').length>0) {
	        	var previewH = $('.hips-container .preview').height();
	            offset = $("#gridcontainer").scrollTop();
	            //previewYloc+$(document).scrollTop()+400>=$("#gridcontainer").height()? offset=$("#gridcontainer").height()-400 : previewYloc+$(document).scrollTop();
	            //$('.hips-container .preview,.hips-container').animate({top:offset},{duration:500,queue:false});
	            $('.hips-container').animate({top:offset},{duration:500,queue:false});
	        }
	    });

	    if (!$.browser.msie) {
	        $('#wrapper > header').hover(
	            function(){$(this).animate({opacity: 1});},
	            function(){$(this).animate({opacity: ($(document).scrollTop()<=10? 1 : 0.8)});}
	        );
	    }

	}
	
	function initCalendar(){
		var view="week";          
        
        var DATA_FEED_URL = "daylogcalendar/list.do";
        var op = {
            view: view,
            theme:3,
            showday: new Date(),
            EditCmdhandler:Edit,
            DeleteCmdhandler:Delete,
            ViewCmdhandler:View,    
            onWeekOrMonthToDay:wtd,
            onBeforeRequestData: cal_beforerequest,
            onAfterRequestData: cal_afterrequest,
            onRequestDataError: cal_onerror, 
            autoload:true,
            url: DATA_FEED_URL,  
            quickAddUrl: "daylogcalendar/quickadd.do", 
            quickUpdateUrl: "daylogcalendar/quickupdate.do",
            quickDeleteUrl: "daylogcalendar/quickremove.do"  
        };
        //var $dv = $("#calhead");
        var _MH = document.documentElement.clientHeight;
        var topH = $("#frame-top").height();
        //var dvH = $dv.height() + 2;
        //op.height = _MH - dvH - topH;
        op.height = _MH - topH - 51;
        op.eventItems =[];

        var p = $("#gridcontainer").bcalendar(op).BcalGetOp();
        $(window).on("resize",function(){
        	var h1 = document.documentElement.clientHeight;
            var h2 = $("#frame-top").height();
        	if($("#dvwkcontaienr").length > 0 || $("#mvcontainer").length > 0){
                var height = h1 - h2 - 51;
            	$("#gridcontainer").resize(height);
        	}else{
        		$("#gridcontainer").height(h1 - h2 - 59);
        	}
		});
        if (p && p.datestrshow) {
            $("#txtdatetimeshow").text(p.datestrshow);
        }
        $("#caltoolbar").noSelect();
        
        $("#hdtxtshow").datepicker({ picker: "#txtdatetimeshow", showtarget: $("#txtdatetimeshow"),
        onReturn:function(r){                          
                        var p = $("#gridcontainer").gotoDate(r).BcalGetOp();
                        if (p && p.datestrshow) {
                            $("#txtdatetimeshow").text(p.datestrshow);
                        }
                 } 
        }); 
        function cal_beforerequest(type)
        {
            var t="Loading data...";
            switch(type)
            {
                case 1:
                    t="Loading data...";
                    break;
                case 2:                      
                case 3:  
                case 4:    
                    t="The request is being processed ...";                                   
                    break;
            }
            $("#errorpannel").hide();
            $("#loadingpannel").html(t).show();    
        }
        function cal_afterrequest(type)
        {
            switch(type)
            {
                case 1:
                    $("#loadingpannel").hide();
                    break;
                case 2:
                case 3:
                case 4:
                    $("#loadingpannel").html("Success!");
                    window.setTimeout(function(){ $("#loadingpannel").hide();},2000);
                break;
            }              
           
        }
        function cal_onerror(type,data)
        {
            $("#errorpannel").show();
        }
        function Edit(data)
        {
           //var eurl="main.html#newdaylog.html?id={0}&start={2}&end={3}&isallday={4}&title={1}";   
           var eurl="daylogcalendar/edit.html?id="+data[0];
           
          // var url = $('a[data-href].current').data("href");;
       		//var caption = $('a[data-href].current').data("title");;
       		//window.location.hash = "#undefined";
       		//var data = {id:data[0]};
       		$.change(eurl, "编辑Daylog", false);
       		
            //if(data)
            //{
            //    var url = StrFormat(eurl,data);
            //    OpenModelWindow(url,{ width: 600, height: 400, caption:"Manage  The Calendar",onclose:function(){
            //       $("#gridcontainer").reload();
            //    }});
            //}
        }    
        function View(data)
        {
            var str = "";
            $.each(data, function(i, item){
                str += "[" + i + "]: " + item + "\n";
            });
            alert(str);               
        }    
        function Delete(data,callback)
        {           
            
            $.alerts.okButton="Ok";  
            $.alerts.cancelButton="Cancel";  
            hiConfirm("Are You Sure to Delete this Event", 'Confirm',function(r){ r && callback(0);});           
        }
        function wtd(p)
        {
           if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }
            $("#caltoolbar div.fcurrent").each(function() {
                $(this).removeClass("fcurrent");
            });
            $("#showdaybtn").addClass("fcurrent");
        }
        //to show day view
        $("#showdaybtn").click(function(e) {
        	if($(this).hasClass("fcurrent")){
        		return;
        	}
            //document.location.href="#day";
            $("#caltoolbar div.fcurrent").each(function() {
                $(this).removeClass("fcurrent");
            })
            $(this).addClass("fcurrent");
            var p = $("#gridcontainer").swtichView("day").BcalGetOp();
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }
        });
      //to show day list view
        $("#showdaylistbtn").click(function(e) {
        	if($(this).hasClass("fcurrent")){
        		return;
        	}
            //document.location.href="#day";
            $("#caltoolbar div.fcurrent").each(function() {
                $(this).removeClass("fcurrent");
            })
            $(this).addClass("fcurrent");
            
            loadDayListView();
            
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }
        });
        
        function loadDayListView(){
        	$("#gridcontainer").empty();
            var p = $("#gridcontainer").getParam("day");
            $.ajax( {
	            "dataType": 'HTML',
	            "type": "POST",
	            "url": "daylogcalendar/daylistview.html",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "data": p,
	            "success": function(data){
	            	$("#gridcontainer").html(data);
	            },
	            "error":function(data){
				}
	          } );
        }
        //to show week view
        $("#showweekbtn").click(function(e) {
        	if($(this).hasClass("fcurrent")){
        		return;
        	}
            //document.location.href="#week";
            $("#caltoolbar div.fcurrent").each(function() {
                $(this).removeClass("fcurrent");
            });
            $(this).addClass("fcurrent");
            var p = $("#gridcontainer").swtichView("week").BcalGetOp();
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }

        });
      //to show week list view
        $("#showweeklistbtn").click(function(e) {
        	if($(this).hasClass("fcurrent")){
        		return;
        	}
            //document.location.href="#week";
            $("#caltoolbar div.fcurrent").each(function() {
                $(this).removeClass("fcurrent");
            });
            $(this).addClass("fcurrent");
            
            loadWeekListView();
            
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }

        });
        
        function loadWeekListView(){
        	$("#gridcontainer").empty();
            var p = $("#gridcontainer").getParam("week");
            $.ajax( {
	            "dataType": 'HTML',
	            "type": "POST",
	            "url": "daylogcalendar/daylistview.html",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "data": p,
	            "success": function(data){
	            	$("#gridcontainer").html(data);
	            },
	            "error":function(data){
				}
	          } );
        }
        //to show month view
        $("#showmonthbtn").click(function(e) {
            //document.location.href="#month";
            $("#caltoolbar div.fcurrent").each(function() {
                $(this).removeClass("fcurrent");
            })
            $(this).addClass("fcurrent");
            var p = $("#gridcontainer").swtichView("month").BcalGetOp();
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }
        });
        
      //to show month view
        $("#showmonthlistbtn").click(function(e) {
        	if($(this).hasClass("fcurrent")){
        		return;
        	}
            //document.location.href="#month";
            $("#caltoolbar div.fcurrent").each(function() {
                $(this).removeClass("fcurrent");
            })
            $(this).addClass("fcurrent");
            
            loadMonthListView();
            
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }
        });
        
        function loadMonthListView(){
        	$("#gridcontainer").empty();
            var p = $("#gridcontainer").getParam("month");
            $.ajax( {
	            "dataType": 'HTML',
	            "type": "POST",
	            "url": "daylogcalendar/daylistview.html",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "data": p,
	            "success": function(data){
	            	$("#gridcontainer").html(data);
	            },
	            "error":function(data){
				}
	          } );
        }
        
        //add new agenda
        function addNewEventDialog(){
			var windowId = "daylog-management-add-new-event-window-id";
	    	if($("#"+windowId).length == 0){
	    		
	    		$("<div>").attr("id",windowId).appendTo("body").dialog({ 
	    			title:"新增日程",
	        	    width:500,  
	        	    height:320,  
	        	    modal:false,
	        	    cache: true,
	        	    collapsible:false,
	        	    minimizable:false,
	        	    maximizable:false,
	        	    resizable:true,
	        	    href:"daylog-addnewevent.html",
	        	    buttons:[{
	    				text:'保存',
	    				handler:function(){
	    					if(onAddNewEvent()){
	    						$("#"+windowId).window("destroy");
	    					}
	    				}
	    			},{
	    				text:'取消',
	    				handler:function(){$("#"+windowId).window("destroy");}
	    			}],
	    			onClose:function(){
	    				$(this).window("destroy");
	    			}
	        	});
	    	}else{
	    		$("#"+windowId).window("center");
	    	}
		}
        
        function onAddNewEvent(){
        	var b = false;
        	
        	return b;
        }
        
        $("#showreflashbtn").click(function(e){
            $("#gridcontainer").reload();
        });
        
        //Add a new event
        $("#faddbtn").click(function(e) {
            var url ="daylog/new.html";
            OpenModelWindow(url,{ width: 500, height: 400, caption: "Create New Calendar"});
        });
        //go to today
        $("#showtodaybtn").click(function(e) {
            var p = $("#gridcontainer").gotoDate().BcalGetOp();
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }


        });
        //previous date range
        $("#sfprevbtn").click(function(e) {
            var p = $("#gridcontainer").previousRange().BcalGetOp();
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }

        });
        //next date range
        $("#sfnextbtn").click(function(e) {
            var p = $("#gridcontainer").nextRange().BcalGetOp();
            if (p && p.datestrshow) {
                $("#txtdatetimeshow").text(p.datestrshow);
            }
        });
	}
    
})();
