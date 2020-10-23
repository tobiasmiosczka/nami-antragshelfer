package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.applicationforms.TimeUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserInputDialog{

	private static final int INPUTFIELD_WIDTH = 120;

	abstract class Input<T>{
		private final JLabel label;
		
		public Input(JPanel parent, int index, String description) {
			label = new JLabel(description + ":");

			FontMetrics met = label.getFontMetrics(label.getFont());
			int height = met.getHeight();
			int width = met.stringWidth(label.getText()) + 20;
			label.setBounds(10, 11 + (index * 30), width,  height);
			parent.add(label);
		}
		
		public void showError(boolean error){
			if (error) {
				label.setForeground(Color.RED);
			} else {
				label.setForeground(Color.BLACK);
			}
		}

		public int getWidth() {
			return label.getWidth();
		}

		public void setWidth(int width) {
			label.setSize(width, label.getHeight());
			updateWidth(width);
		}

		public abstract void updateWidth(int width);
		public abstract boolean check();
		protected abstract T getValue();
		public abstract String toString();
	}
	
	private class InputString extends Input<String>{
		final JTextField textField;
		public InputString(JPanel parent, int index, String description, String preview) {
			super(parent, index, description);
			textField = new JTextField(preview);
			textField.setBounds(this.getWidth() + 10,  11 + (index * 30), INPUTFIELD_WIDTH,  20);
			parent.add(textField);
		}

		@Override
		public void updateWidth(int width) {
			textField.setBounds(width, textField.getY(), textField.getWidth(), textField.getHeight());
		}

		@Override
		public boolean check() {
			return true;
		}

		@Override
		public String getValue() {
			return textField.getText();
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	private class InputInteger extends Input<Integer>{
		final JTextField textField;
		public InputInteger(JPanel parent, int index, String description, int preview) {
			super(parent, index, description);
			textField = new JTextField(preview);
			textField.setBounds(this.getWidth() + 10,  11 + (index * 30), INPUTFIELD_WIDTH,  20);
			parent.add(textField);
		}

		@Override
		public void updateWidth(int width) {
			textField.setBounds(width, textField.getY(), textField.getWidth(), textField.getHeight());
		}

		@Override
		public boolean check() {
			try {
				Integer.parseInt(textField.getText());
		    } catch(NumberFormatException e) {
		        return false;
		    }
		    return true;
		}

		@Override
		public Integer getValue() {
			return Integer.getInteger(textField.getText());
		}

		@Override
		public String toString() {
			return String.valueOf(getValue());
		}

	}

	private class InputFloat extends Input<Float> {

		final JTextField textField;
		public InputFloat(JPanel parent, int index, String description, float preview) {
			super(parent, index, description);
			textField = new JTextField(String.valueOf(preview));
			textField.setBounds(this.getWidth() + 10,  11 + (index * 30), INPUTFIELD_WIDTH,  20);
			parent.add(textField);
		}

		@Override
		public void updateWidth(int width) {
			textField.setBounds(width, textField.getY(), textField.getWidth(), textField.getHeight());
		}

		@Override
		public boolean check() {
			try {
				Float.parseFloat(textField.getText());
			} catch(NumberFormatException e) {
				return false;
			}
			return true;
		}

		@Override
		public Float getValue() {
			return Float.parseFloat(textField.getText());
		}

		@Override
		public String toString() {
			return String.valueOf(getValue());
		}
	}
	
	private class InputDate extends Input<LocalDate> {
		final JTextField textField;
		public InputDate(JPanel parent, int index, String description, LocalDate preview) {
			super(parent, index, description);
			textField = new JTextField(TimeUtil.getDateString(preview));
			textField.setBounds(this.getWidth() + 10,  11 + (index * 30), INPUTFIELD_WIDTH,  20);
			parent.add(textField);
		}

		@Override
		public void updateWidth(int width) {
			textField.setBounds(width, textField.getY(), textField.getWidth(), textField.getHeight());
		}

		@Override
		public boolean check() {
			try {
				TimeUtil.parseDate(textField.getText());
			} catch (DateTimeParseException e) {
				return false;
			}
			return true;
		}

		@Override
		public LocalDate getValue() {
			try {
				return TimeUtil.parseDate(textField.getText());
			} catch (DateTimeParseException e) {
				return null;
			}
		}

		@Override
		public String toString() {
			return TimeUtil.getDateString(getValue());
		}	
	}
	
	private class InputBoolean extends Input<Boolean>{
		final JCheckBox checkBox;
		public InputBoolean(JPanel parent, int index, String description, boolean preview) {
			super(parent, index, description);
			checkBox = new JCheckBox();
			checkBox.setSelected(preview);
			checkBox.setBounds(this.getWidth() + 10,  11 + (index * 30), INPUTFIELD_WIDTH,  20);
			parent.add(checkBox);
		}

		@Override
		public void updateWidth(int width) {
			checkBox.setBounds(width, checkBox.getY(), checkBox.getWidth(), checkBox.getHeight());
		}

		@Override
		public boolean check() {
			return true;
		}

		@Override
		public Boolean getValue() {
			return checkBox.isSelected();
		}

		@Override
		public String toString() {
			return String.valueOf(checkBox.isSelected());
		}
		
	}
	
	private final JDialog dialog;
	private final JPanel panel;

	private final List<Input> inputList = new LinkedList<>();
	private boolean isOK = false;
		
	/**
	 * Create the panel.
	 */
	public UserInputDialog(JFrame owner) {
		dialog = new JDialog(owner, "Optionen", true);
		dialog.setBounds(owner.getX(), owner.getY(), dialog.getWidth(), dialog.getHeight());
		dialog.setResizable(false);
		dialog.getContentPane().setLayout(new BorderLayout(0, 0));
		panel = new JPanel();
		dialog.getContentPane().add(panel);
		panel.setLayout(null);
		JPanel panelOkCancel = new JPanel();
		dialog.getContentPane().add(panelOkCancel, BorderLayout.SOUTH);

		JButton btnOK = new JButton("Fertig");
		btnOK.addActionListener(e -> {
			if(check()){
				isOK = true;
				dialog.setVisible(false);
			}
		});
		panelOkCancel.add(btnOK);

		JButton btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(e -> {
			isOK = false;
			dialog.setVisible(false);
		});
		panelOkCancel.add(btnCancel);
	}

	private void updateBounds() {
		int width = 0;
		for(Input input : inputList) {
			if (input.getWidth() > width) {
				width = input.getWidth();
			}
		}
		for(Input input : inputList) {
			input.updateWidth(width);
		}
		dialog.setBounds(dialog.getX(), dialog.getY(), width + INPUTFIELD_WIDTH, (inputList.size() * 30) + 80);
	}
	
	public void addStringOption(String description, String preview) {
		inputList.add(new InputString(panel, inputList.size(), description, preview));
		updateBounds();
	}

	public void addIntegerOption(String description, int preview) {
		inputList.add(new InputInteger(panel, inputList.size(), description, preview));
		updateBounds();
	}

	public void addFloatOption(String description, float preview) {
		inputList.add(new InputFloat(panel, inputList.size(), description, preview));
		updateBounds();
	}

	public void addDateOption(String description, LocalDate preview){
		inputList.add(new InputDate(panel, inputList.size(), description, preview));
		updateBounds();
	}

	public void addBooleanOption(String description, boolean preview) {
		inputList.add(new InputBoolean(panel, inputList.size(), description, preview));
		updateBounds();
	}
	
	public boolean showModal(){
		if(getOptionCount() == 0){
			return true;
		}
		dialog.setVisible(true);
		return isOK;
	}

	public Object getOption(int index){
		return inputList.get(index).getValue();
	}

	private boolean check(){
		boolean valid = true;
		for (Input input : inputList) {
			if (!input.check()) {
				input.showError(true);
				valid = false;
			} else {
				input.showError(false);
			}
		}
		return valid;
	}

	private int getOptionCount() {
		return inputList.size();
	}
}