package model.entity

import java.util.UUID

import org.joda.time.DateTime
import CustomUnicornPlay._
import CustomUnicornPlay.driver.simple._


case class PipelineEvaluationId(id: Long) extends AnyVal with BaseId
object PipelineEvaluationId extends IdCompanion[PipelineEvaluationId]

case class PipelineEvaluation(
  id: Option[PipelineEvaluationId],
  pipelineId: PipelineId,
  isFinished: Boolean,
  isSuccess: Option[Boolean],
  var uuid: String = UUID.randomUUID().toString,
  var createdUtc: Option[DateTime] = None,
  var modifiedUtc: Option[DateTime] = None
  ) extends IdEntity[PipelineEvaluationId] {

  def pipeline(implicit session: Session) : Pipeline = {
    (for {
      p <- pipelinesQuery if p.id === pipelineId
    } yield p).first
  }

  def results(implicit session: Session) : Seq[PipelineEvaluationResult] = {
    (for {
      r <- pipelineEvaluationResultsQuery if r.pipelineEvaluationId === id
    } yield r).list
  }

}


class PipelineEvaluationTable(tag: Tag) extends IdEntityTable[PipelineEvaluationId, PipelineEvaluation](tag, "pipeline_evaluation") {

  def isFinished = column[Boolean]("is_finished", O.NotNull)

  def isSuccess = column[Option[Boolean]]("is_success")

  def pipelineId = column[PipelineId]("pipeline_id")

  def pipeline = foreignKey("pipeline", pipelineId, pipelinesQuery)(_.id)

  def * = (id.?, pipelineId, isFinished, isSuccess, uuid, createdUtc, modifiedUtc) <> (PipelineEvaluation.tupled, PipelineEvaluation.unapply _)
}