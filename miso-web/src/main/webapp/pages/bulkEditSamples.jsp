<%--
  ~ Copyright (c) 2012. The Genome Analysis Centre, Norwich, UK
  ~ MISO project contacts: Robert Davey, Mario Caccamo @ TGAC
  ~ **********************************************************************
  ~
  ~ This file is part of MISO.
  ~
  ~ MISO is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ MISO is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with MISO.  If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ **********************************************************************
  --%>

<%--
  Created by IntelliJ IDEA.
  User: davey
  Date: 15-Feb-2010
  Time: 15:09:06
--%>
<%@ include file="../header.jsp" %>
<script src="<c:url value='/scripts/jquery/js/jquery.breadcrumbs.popup.js'/>" type="text/javascript"></script>

<script src="<c:url value='/scripts/jquery/datatables/js/jquery.dataTables.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/scripts/jquery/editable/jquery.jeditable.mini.js'/>" type="text/javascript"></script>
<script src="<c:url value='/scripts/jquery/editable/jquery.jeditable.datepicker.js'/>" type="text/javascript"></script>
<script src="<c:url value='/scripts/jquery/editable/jquery.jeditable.checkbox.js'/>" type="text/javascript"></script>
<link href="<c:url value='/scripts/jquery/datatables/css/jquery.dataTables.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/scripts/handsontable/dist/pikaday/pikaday.css'/>" rel="stylesheet" type="text/css" />
<script src="<c:url value='/scripts/handsontable/dist/pikaday/pikaday.js'/>" type="text/javascript"></script>
<script src="<c:url value='/scripts/handsontable/dist/moment/moment.js'/>" type="text/javascript"></script>

<script src="<c:url value='/scripts/datatables_utils.js'/>" type="text/javascript"></script>
<script src="<c:url value='/scripts/natural_sort.js'/>" type="text/javascript"></script>

<script src="<c:url value='/scripts/handsontable/dist/handsontable.full.js'/>" type="text/javascript"></script>
<script src="<c:url value='/scripts/handsontable_renderers.js'/>" type="text/javascript"></script>
<link rel="stylesheet" media="screen" href="/scripts/handsontable/dist/handsontable.full.css">
<script src="<c:url value='/scripts/sample_hot.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/scripts/parsley/parsley.min.js'/>"></script>

<div id="maincontent">
<div id="contentcolumn">

	<h1>Edit Samples</h1>
	
	<div id="HOTbulkForm" data-detailed-sample="${detailedSample}">
	
		<div id="saveSuccesses"  class="parsley-success hidden">
	    <p id="successMessages"></p>
	  </div>
	    <div id="saveErrors" class="bs-callout bs-callout-warning hidden">
	      <h2>Oh snap!</h2>
	      <p>The following rows failed to save:</p>
	      <p id="errorMessages"></p>
	    </div>
		<button id="saveSamples">Save</button>
		<c:if test="${detailedSample}">
			<button id="addQcs" onclick="Sample.hot.regenerateWithQcs();">Add QCs</button>
			<button id="hideAddnalCols" onclick="Sample.hot.hideAdditionalCols();">Hide Extra Columns</button>
		</c:if>
		
		<div id="hotContainer"></div>
	
	</div>
	
	<script type="text/javascript">
	  jQuery(document).ready(function () {
	    Sample.hot.samplesJSON = ${samplesJSON};
	    Sample.hot.dropdownRef = ${referenceDataJSON};
	    Sample.hot.aliasGenerationEnabled = ${aliasGenerationEnabled};
	    Sample.hot.detailedSample = JSON.parse(document.getElementById('HOTbulkForm').dataset.detailedSample);
	    Sample.hot.button = document.getElementById('saveSamples');
	    Sample.hot.createOrEdit = "${method}";

	    Sample.hot.makeBulkEditTable = function () {
        Sample.hot.samplesJSON = Sample.hot.modifySamplesForEdit(Sample.hot.samplesJSON);
        var sampleCategory = Sample.hot.getCategoryFromClassId(Sample.hot.samplesJSON[0].sampleAdditionalInfo.sampleClassId);
        Sample.hot.makeHOT(Sample.hot.samplesJSON, false, sampleCategory, false);
      };

      Sample.hot.makeBulkCreateTable = function () {
        Sample.hot.newSamplesJSON = Sample.hot.modifySamplesForPropagate(Sample.hot.samplesJSON);
        var sampleCategory = Sample.hot.getCategoryFromClassId(Sample.hot.newSamplesJSON[0].sampleAdditionalInfo.parentSampleClassId);
        Sample.hot.makeHOT(Sample.hot.newSamplesJSON, false, sampleCategory, false);
        Sample.hot.hotTable.updateSettings({
          cells: function (row, col, prop) {
            var cellProperties = {};
            if (prop == 'sampleAdditionalInfo.sampleClassAlias') {
              cellProperties.readOnly = false;
            }
            return cellProperties;
          }
        });
      };

	    // get SampleOptions and make the appropriate table
      if (Boolean(Sample.hot.detailedSample)) {
        if (Sample.hot.createOrEdit == "create") {
          Sample.hot.sampleClassId = parseInt(${sampleClassId});
          Sample.hot.button.addEventListener('click', Sample.hot.propagateData, true);
          Sample.hot.fetchSampleOptions(Sample.hot.makeBulkCreateTable);
        } else {
          Sample.hot.button.addEventListener('click', Sample.hot.updateData, true);
          Sample.hot.fetchSampleOptions(Sample.hot.makeBulkEditTable);
        }
      }
	  });
	</script>
	
	<div>
    <c:forEach items="${samples}" var="sample">
        <h2>${sample.name}</h2>
        <p>id: ${sample.id}</p>
    </c:forEach>
	</div>

</div>
</div>
<%@ include file="adminsub.jsp" %>

<%@ include file="../footer.jsp" %>