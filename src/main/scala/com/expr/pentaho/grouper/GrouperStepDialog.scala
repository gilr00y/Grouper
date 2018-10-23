package com.expr.pentaho.grouper

import org.eclipse.swt.SWT
import org.eclipse.swt.events.{ModifyEvent, ModifyListener}
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.pentaho.di.core.Const
import org.pentaho.di.trans._
import org.pentaho.di.trans.step._
import org.pentaho.di.ui.core.widget.{ColumnInfo, TableView}
import org.pentaho.di.ui.trans.step._

class GrouperStepDialog(parent: Shell, m: Object, transMeta: TransMeta, stepName: String)
    extends BaseStepDialog(parent, m.asInstanceOf[BaseStepMeta], transMeta, stepName)
    with StepDialogInterface {

  this.shell = parent

  private[this] val stepMeta = m.asInstanceOf[StepMetaInterface]
  private[this] val ourMeta = m.asInstanceOf[GrouperStepMeta]

  val middle = props.getMiddlePct
  val margin = Const.MARGIN

  def open(): String = {
    val parent = getParent
    val display = parent.getDisplay

    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN )
    shell.setText("Grouper")
    props.setLook(shell)
    setShellImage(shell, stepMeta)

    val layout = new FormLayout
    layout.marginWidth = Const.FORM_MARGIN
    layout.marginHeight = Const.FORM_MARGIN
    shell.setLayout(layout)
    val stepName = makeRow(shell, "Step Name:", stepname, None)

    val wlGroup = new Label( shell, SWT.NONE)
    wlGroup.setText("GROUP BY columns")
    props.setLook( wlGroup )

    val fdlGroup = new FormData
    fdlGroup.left = new FormAttachment( 0,0 )
    fdlGroup.top = new FormAttachment(stepName, margin)
    wlGroup.setLayoutData( fdlGroup )

    val nrKeyCols: Int = 1
    val nrKeyRows: Int = 1//Option(ourMeta.getGroupFields).getOrElse( List(null)).length
    val lsMod = new ModifyListener {
      def modifyText(e: ModifyEvent): Unit = {
        ourMeta.setChanged
      }
    }
    var ciKey: Array[ColumnInfo] = Array(new ColumnInfo( "Column", ColumnInfo.COLUMN_TYPE_CCOMBO))
//    ciKey(0) = new ColumnInfo( "Column", ColumnInfo.COLUMN_TYPE_CCOMBO)
    val wGroup = new TableView(
      transMeta, shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, ciKey,
      nrKeyRows, lsMod, props
    )

    def get(): Unit = {
      val r = transMeta.getPrevStepFields(stepname)
      if (r != null && !r.isEmpty) {
        BaseStepDialog.getFieldsFromPrevious(
          r, wGroup, 1, Array[Int](1),
          new Array[Int](1), -1, -1, null
        )
      }
    }
    val lsGet = new Listener() {
      def handleEvent(e: Event): Unit = {
        get
      }
    }

    val wGet = new Button(shell, SWT.PUSH)
    wGet.setText( "Get Fields" )
    fdGet = new FormData
    fdGet.top = new FormAttachment(wlGroup, margin)
    fdGet.right = new FormAttachment(100, 0)
    wGet.setLayoutData(fdGet)
    wGet.addListener(SWT.Selection, lsGet)

    val fdGroup = new FormData
    fdGroup.left = new FormAttachment(0, 0)
    fdGroup.top = new FormAttachment(wlGroup, margin)
    fdGroup.right = new FormAttachment(wGet, -margin)
    fdGroup.bottom = new FormAttachment(45, 0)
    wGroup.setLayoutData(fdGroup)

    // val projectId = makeRow(shell, "IronMQ Project Id (blank to locate in .json config):", ourMeta.projectId, Some(token))
    //   val queue = makeRow(shell, "IronMQ Queue Name (required):", ourMeta.queue, Some(projectId))
    //   val outputField = makeRow(shell, "Output field name:", ourMeta.outputField, Some(queue))

 

    val okButton = new Button(shell, SWT.PUSH)
    okButton.setText("OK")
    okButton.addListener(SWT.Selection, new Listener() {
      def handleEvent(e: Event): Unit = {
        // if (stepName.getText.nonEmpty && queue.getText.nonEmpty) {
        stepname = stepName.getText
        // ourMeta.projectId = projectId.getText
        // ourMeta.queue = queue.getText
        // ourMeta.outputField = outputField.getText
        ourMeta.setChanged(true)

        shell.dispose()
      }
    })

    val cancelButton = new Button(shell, SWT.PUSH)
    cancelButton.setText("Cancel")
    cancelButton.addListener(SWT.Selection, new Listener {
      def handleEvent(e: Event): Unit = {
        stepname = null
        ourMeta.setChanged(false)
        shell.dispose()
      }
    })

  //   setButtonPositions(Array(okButton, cancelButton), margin, outputField)
  //   setSize()

    shell.pack()
    shell.open()
    while ( !shell.isDisposed ) {
      if ( !display.readAndDispatch() ) {
        display.sleep()
      }
    }
    stepname
  }

  def makeRow(shell: Shell, labelText: String, initialValue: String, relativeTo: Option[Control]): Text = {
    val topForm = relativeTo match {
      case Some(control) => new FormAttachment(control, margin)
      case None => new FormAttachment(0, margin)
    }

    val label = new Label(shell, SWT.RIGHT)
    label.setText(labelText)
    props.setLook(label)
    label.setLayoutData {
      val formData = new FormData
      formData.left = new FormAttachment(0,margin)
      formData.top = topForm
      formData.right = new FormAttachment(middle, -margin)
      formData
    }

    val field = new Text(shell, SWT.LEFT | SWT.BORDER)
    field.setText(initialValue)
    props.setLook(field)
    field.setLayoutData {
      val formData = new FormData
      formData.left = new FormAttachment(middle,0)
      formData.top = topForm
      formData.right = new FormAttachment(100, 0)
      formData
    }

    field
  }
}
