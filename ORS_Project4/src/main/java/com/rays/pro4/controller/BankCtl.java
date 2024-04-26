package com.rays.pro4.controller;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.BankBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.BankModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet (name = "BankCtl" , urlPatterns = {"/ctl/BankCtl"})
public class BankCtl extends BaseCtl{
	@Override
	protected boolean validate(HttpServletRequest request) {
		String op = request.getParameter("operation");
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name",PropertyReader.getValue("error.require", "Name is") );
			pass=false;
		}else if(!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "name must contain alfabets only");
			pass=false;
		}
		
		if (DataValidator.isNull(request.getParameter("amount"))) {
			request.setAttribute("amount",PropertyReader.getValue("error.require", "amount is") );
			pass=false;
		}else if(!DataValidator.isInteger(request.getParameter("amount"))) {
			request.setAttribute("amount", "amount must contain interger only");
			pass=false;
		}
		
		return pass;
	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		BankBean bean = new BankBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setAmount(DataUtility.getString(request.getParameter("amount")));
		
		populateDTO(bean, request);
		
		return bean;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String op =	request.getParameter("opreation");
	BankBean bean = null;
	BankModel model = new BankModel();
	long id = DataUtility.getLong(request.getParameter("id"));
	if (id>0 || op != null) {
		
		try {
			bean = model.findByPK(id);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServletUtility.setBean(bean , request);
		
	}
	ServletUtility.forward(getView(), request, response);
		
		
		
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		BankModel model = new BankModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			BankBean bean = (BankBean) populateBean(request);
			

			if (id > 0) {

				try {
					model.update(bean);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DuplicateRecordException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServletUtility.setBean(bean, request);
				
				ServletUtility.setSuccessMessage("  successfully Updated", request);

			} else {
				System.out.println(" U ctl DoPost 33333");
				long pk = 0;
				try {
					pk = model.add(bean);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DuplicateRecordException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ServletUtility.setSuccessMessage(" successfully Added", request);

				bean.setId(pk);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			

			BankBean bean = (BankBean) populateBean(request);
			try {
				model.delete(bean);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ServletUtility.redirect(ORSView.BANK_CTL, request, response);
			return;

		} 
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.BANK_VIEW;
	}
	
	

}

