/*
	>>> THIS IS CALENDAR TEMPLATE FILE <<<
	
	Variable defined here (CAL_TPL) should be passed to calendar constructor
	as second parameter.
	

	Notes:

	- Same template structure can be used for multiple
	calendar instances.
	- When specifying not numeric values for HTML tag attributes make sure you
	put them in apostrophes

*/
var CAL_TPLPlusMinusOculta = {

	// >>> Localization settings <<<
	
	
	// months as they appear in the selection list
	'months': ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],

	// week day titles as they appear on the calendar
	'weekdays': ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],

	// day week starts from (normally 1-Mo or 0-Su)
	'weekstart': 1,
	
	// width of popup window (for Netscape 4.x only)
	'w': 190, 
	// height of popup window (for Netscape 4.x only)
	'h': 180,
	// start z-inxex - only for modal mode
	'startzindex': 0,
	// >>> Navbar settings <<<

	// in year selection box how many years to list relative to current year
	'yearsbefore': 4,
	'yearsafter': 4,
	
	// >>> Appearence settings (HTML tags attributes) <<<

	// outer table (TABLE)
	'outertable': {
		'cellpadding': 1,
		'cellspacing': 0,
		'border': 0,
		'bgcolor': '#ffffff',
		'width': 180
	},
	// month & year navigation table (TABLE)
	'navtable': {
		'cellpadding': 0,
		'cellspacing': 0,
		'border': 1,
		'bordercolor': '#ffffff',
		'width': '100%'
	},
	// today icon cell (TD); if omited no today button will be displayed
	'todaycell': {
		'width' : 10
	},
	// time navigation table (TABLE)
	'timetable': {
		'cellpadding': 0,
		'cellspacing': 0,
		'border': 1,
		'width': '100%',
		'class' : 'calTimetable'
	},
	// pixel image (IMG)
	// for modal mode only
	'pixel': {
		'src': './images/pixel.gif',
		'width': 1,
		'height': 1,
		'border' : 0		
	},
	// not for on-page mode
	'calbutton': {
		'value': 'seleccione una fecha',
		'valign': 'bottom'
	},
	
	// icon image to open the calendar instance (IMG), 
	// not for on-page mode
	
	'caliconshow': {
		'src': './images/btnCalendar.gif',
		'width': 18,
		'height': 18,
		'border' : 0,
		'alt': 'seleccione una fecha',
		'valign': 'bottom'
	},
	// icon image to close the calendar instance (IMG)
	// for modal mode only
	'caliconhide': {
		'src': './images/btnCalendar.gif',
		'width': 18,
		'height': 18,
		'border' : 0,
		'alt': 'seleccione una fecha',
		'valign': 'bottom'
	},
	// input text field to store the date & time selected (INPUT type="text")
	'datacontrol': {
		'class' : 'calDatCtrl'
	},
	// hour, minute & second selectors (SELECT)
	'timeselector': {
		'class' : 'calTimeselector'
	},
	// today icon image (IMG); if omited no today button will be displayed
	'todayimage': {
		'src': './images/today.gif',
		'width': 10,
		'height': 20,
		'border' : 0,
		'alt': 'volver a la fecha actual'
	},
	// month scroll icon cell (TD)
	'monthscrollcell': {
		'width' : 10
	},
	// next hour image (IMG)
	'hourplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'hora siguiente'
	},
	// previous hour image (IMG)
	'hourminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'hora anterior'
	},
	// next minute image (IMG)
	'minplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'minuto siguiente'
	},
	// previous minute image (IMG)
	'minminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'minuto anterior'
	},
	// next second image (IMG)
	'secplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'segundo siguiente'
	},
	// previous second image (IMG)
	'secminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'segundo anterior'
	},
	// next month image (IMG)
	'monthplusimage': {
		'src': '',
		'width': 0,
		'height': 0,
		'border' : 0,
		'alt': 'mes siguiente'
	},
	// previous month image (IMG)
	'monthminusimage': {
		'src': '',
		'width': 0,
		'height': 0,
		'border' : 0,
		'alt': 'mes anterior'
	},
	// year scroll icon cell (TD)
	'yearscrollcell': {
		'width' : 10
	},
	// next year image (IMG)
	'yearplusimage': {
		'src': '',
		'width': 0,
		'height': 0,
		'border' : 0,
		'alt': 'año siguiente'
	},
	// previous year image (IMG)
	'yearminusimage': {
		'src': '',
		'width': 0,
		'height': 0,
		'border' : 0,
		'alt': 'año anterior'
	},
	// next AM/PM image (IMG)
	'applusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'scroll AM'
	},
	// previous AM/PM image (IMG)
	'apminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'scroll PM'
	},
	
	
	
		// inactive next image for alternative title (IMG)
	'disyearplusimagealt': {
		'src': './images/plus_year_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// inactive next image for alternative title (IMG)
	'disyearminusimagealt': {
		'src': './images/minus_year_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// inactive next image for alternative title (IMG)
	'dismonthplusimagealt': {
		'src': './images/plus_month_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// inactive next image for alternative title (IMG)
	'dismonthminusimagealt': {
		'src': './images/minus_month_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// next month image   for alternative toolba(IMG)
	'monthplusimagealt': {
		'src': './images/plus_month_maqueta.gif',
		'width': 13,
		'height': 13,
		'border': 0,
		'alt': 'mes siguiente'
	},
	// previous month image  for alternative toolba(IMG)
	'monthminusimagealt': {
		'src': './images/minus_month_maqueta.gif',
		'width': 13,
		'height': 13,
		'border': 0,
		'alt': 'mes anterior'
	},
	// next year image for alternative toolbar(IMG)
	'yearplusimagealt': {
		'src': './images/plus_year_maqueta.gif',
		'width': 13,
		'height': 13,
		'border' : 0,
		'alt': 'año siguiente'
	},
	// previous year image  for alternative toolbar(IMG)
	'yearminusimagealt': {
		'src': './images/minus_year_maqueta.gif',
		'width': 13,
		'height': 13,
		'border' : 0,
		'alt': 'año anterior'
	},
	'datatitle' : {
		'class': 'calDataTitle'
	},
	
	
	
	
	// inactive next image (IMG)
	'displusimage': {
		'src': './images/plus_dis.gif',
		'width': 10,
		'height': 10,
		'border' : 0
	},
	// inactive previous image (IMG)
	'disminusimage': {
		'src': './images/minus_dis.gif',
		'width': 10,
		'height': 10,
		'border' : 0
	},
	// month selector cell (TD)
	'monthselectorcell': {
		'width': '50px',
		'align': 'right'
	},
	// hour, minute & second scroll icon cell (TD)
	'timescrollcell': {
		'width' : 10
	},
	// time selector cell (TD)
	'timeselectorcell': {
		'width': '50px',
		'align': 'right'
	},
	// month selector (SELECT)
	'monthselector': {
		'class': 'calMonthselector'
	},
	// year selector cell (TD)
	'yearselectorcell': {
		'align': 'right'
	},
	// year selector (SELECT)
	'yearselector': {
		'class': 'calYearselector'
	},
	// cell containing calendar grid (TD)
	'gridcell' : {},
	// calendar grid (TABLE)
	'gridtable': {
		'cellpadding': 2,
		'cellspacing': 2,
		'border': 0,
		'width': '100%'
	},
	// week day title cell (TD)
	'wdaytitle' : {
		'width' : 5,
		'class' : 'calWTitle'
	},
	// other month day text (A/SPAN)
	'dayothermonth': {
		'class': 'calOtherMonth'
	},
	// forbidden day text (A/SPAN)
	'dayforbidden': {
		'class': 'calForbDate'
	},
	// default day text (A/SPAN)
	'daynormal': {
		'class': 'calThisMonth'
	},
	// today day text (SPAN)
	'daytodaycell': {
		'class': 'calDayToday'
	},
	// selected day cell (TD)
	'dayselectedcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayCurrent'
	},
	// wekend day cell (TD)
	'dayweekendcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayWeekend'
	},
	// marked day cell (TD)
	'daymarkedcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayHoliday'
	},
	// working day cell (TD)
	'daynormalcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayWorking'
	}
};
var CAL_TPL = {

	// >>> Localization settings <<<
	
	
	// months as they appear in the selection list
	'months': ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],

	// week day titles as they appear on the calendar
	'weekdays': ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],

	// day week starts from (normally 1-Mo or 0-Su)
	'weekstart': 1,
	
	// width of popup window (for Netscape 4.x only)
	'w': 190, 
	// height of popup window (for Netscape 4.x only)
	'h': 180,
	// start z-inxex - only for modal mode
	'startzindex': 0,
	// >>> Navbar settings <<<

	// in year selection box how many years to list relative to current year
	'yearsbefore': 4,
	'yearsafter': 4,
	
	// >>> Appearence settings (HTML tags attributes) <<<

	// outer table (TABLE)
	'outertable': {
		'cellpadding': 1,
		'cellspacing': 0,
		'border': 0,
		'bgcolor': '#ffffff',
		'width': 180
	},
	// month & year navigation table (TABLE)
	'navtable': {
		'cellpadding': 0,
		'cellspacing': 0,
		'border': 1,
		'bordercolor': '#ffffff',
		'width': '100%'
	},
	// today icon cell (TD); if omited no today button will be displayed
	'todaycell': {
		'width' : 10
	},
	// time navigation table (TABLE)
	'timetable': {
		'cellpadding': 0,
		'cellspacing': 0,
		'border': 1,
		'width': '100%',
		'class' : 'calTimetable'
	},
	// pixel image (IMG)
	// for modal mode only
	'pixel': {
		'src': 'img/pixel.gif',
		'width': 1,
		'height': 1,
		'border' : 0		
	},
	// not for on-page mode
	'calbutton': {
		'value': 'seleccione una fecha',
		'valign': 'bottom'
	},
	
	// icon image to open the calendar instance (IMG), 
	// not for on-page mode
	
	'caliconshow': {
		'src': './images/btnCalendar.gif',
		'width': 18,
		'height': 18,
		'border' : 0,
		'alt': 'seleccione una fecha',
		'valign': 'bottom'
	},
	// icon image to close the calendar instance (IMG)
	// for modal mode only
	'caliconhide': {
		'src': './images/btnCalendar.gif',
		'width': 18,
		'height': 18,
		'border' : 0,
		'alt': 'seleccione una fecha',
		'valign': 'bottom'
	},
	// input text field to store the date & time selected (INPUT type="text")
	'datacontrol': {
		'class' : 'calDatCtrl'
	},
	// hour, minute & second selectors (SELECT)
	'timeselector': {
		'class' : 'calTimeselector'
	},
	// today icon image (IMG); if omited no today button will be displayed
	'todayimage': {
		'src': './images/today.gif',
		'width': 10,
		'height': 20,
		'border' : 0,
		'alt': 'volver a la fecha actual'
	},
	// month scroll icon cell (TD)
	'monthscrollcell': {
		'width' : 10
	},
	// next hour image (IMG)
	'hourplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'hora siguiente'
	},
	// previous hour image (IMG)
	'hourminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'hora anterior'
	},
	// next minute image (IMG)
	'minplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'minuto siguiente'
	},
	// previous minute image (IMG)
	'minminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'minuto anterior'
	},
	// next second image (IMG)
	'secplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'segundo siguiente'
	},
	// previous second image (IMG)
	'secminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'segundo anterior'
	},
	// next month image (IMG)
	'monthplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'mes siguiente'
	},
	// previous month image (IMG)
	'monthminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'mes anterior'
	},
	// year scroll icon cell (TD)
	'yearscrollcell': {
		'width' : 10
	},
	// next year image (IMG)
	'yearplusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'año siguiente'
	},
	// previous year image (IMG)
	'yearminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'año anterior'
	},
	// next AM/PM image (IMG)
	'applusimage': {
		'src': './images/plus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'scroll AM'
	},
	// previous AM/PM image (IMG)
	'apminusimage': {
		'src': './images/minus.gif',
		'width': 10,
		'height': 10,
		'border' : 0,
		'alt': 'scroll PM'
	},
	
	
	
		// inactive next image for alternative title (IMG)
	'disyearplusimagealt': {
		'src': './images/plus_year_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// inactive next image for alternative title (IMG)
	'disyearminusimagealt': {
		'src': './images/minus_year_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// inactive next image for alternative title (IMG)
	'dismonthplusimagealt': {
		'src': './images/plus_month_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// inactive next image for alternative title (IMG)
	'dismonthminusimagealt': {
		'src': './images/minus_month_dis.gif',
		'width': 13,
		'height': 13,
		'border': 0
	},
	// next month image   for alternative toolba(IMG)
	'monthplusimagealt': {
		'src': './images/plus_month_maqueta.gif',
		'width': 13,
		'height': 13,
		'border': 0,
		'alt': 'mes siguiente'
	},
	// previous month image  for alternative toolba(IMG)
	'monthminusimagealt': {
		'src': './images/minus_month_maqueta.gif',
		'width': 13,
		'height': 13,
		'border': 0,
		'alt': 'mes anterior'
	},
	// next year image for alternative toolbar(IMG)
	'yearplusimagealt': {
		'src': './images/plus_year_maqueta.gif',
		'width': 13,
		'height': 13,
		'border' : 0,
		'alt': 'año siguiente'
	},
	// previous year image  for alternative toolbar(IMG)
	'yearminusimagealt': {
		'src': './images/minus_year_maqueta.gif',
		'width': 13,
		'height': 13,
		'border' : 0,
		'alt': 'año anterior'
	},
	'datatitle' : {
		'class': 'calDataTitle'
	},
	
	
	
	
	// inactive next image (IMG)
	'displusimage': {
		'src': './images/plus_dis.gif',
		'width': 10,
		'height': 10,
		'border' : 0
	},
	// inactive previous image (IMG)
	'disminusimage': {
		'src': './images/minus_dis.gif',
		'width': 10,
		'height': 10,
		'border' : 0
	},
	// month selector cell (TD)
	'monthselectorcell': {
		'width': '50px',
		'align': 'right'
	},
	// hour, minute & second scroll icon cell (TD)
	'timescrollcell': {
		'width' : 10
	},
	// time selector cell (TD)
	'timeselectorcell': {
		'width': '50px',
		'align': 'right'
	},
	// month selector (SELECT)
	'monthselector': {
		'class': 'calMonthselector'
	},
	// year selector cell (TD)
	'yearselectorcell': {
		'align': 'right'
	},
	// year selector (SELECT)
	'yearselector': {
		'class': 'calYearselector'
	},
	// cell containing calendar grid (TD)
	'gridcell' : {},
	// calendar grid (TABLE)
	'gridtable': {
		'cellpadding': 2,
		'cellspacing': 2,
		'border': 0,
		'width': '100%'
	},
	// week day title cell (TD)
	'wdaytitle' : {
		'width' : 5,
		'class' : 'calWTitle'
	},
	// other month day text (A/SPAN)
	'dayothermonth': {
		'class': 'calOtherMonth'
	},
	// forbidden day text (A/SPAN)
	'dayforbidden': {
		'class': 'calForbDate'
	},
	// default day text (A/SPAN)
	'daynormal': {
		'class': 'calThisMonth'
	},
	// today day text (SPAN)
	'daytodaycell': {
		'class': 'calDayToday'
	},
	// selected day cell (TD)
	'dayselectedcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayCurrent'
	},
	// wekend day cell (TD)
	'dayweekendcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayWeekend'
	},
	// marked day cell (TD)
	'daymarkedcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayHoliday'
	},
	// working day cell (TD)
	'daynormalcell': {
		'align': 'center',
		'valign': 'middle',
		'class': 'calDayWorking'
	}
};