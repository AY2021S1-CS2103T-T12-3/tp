package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

import seedu.address.logic.commands.LessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Date;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Time;
import seedu.address.model.task.DateTime;
import seedu.address.model.task.Description;
import seedu.address.model.task.Title;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class LessonCommandParser implements Parser<LessonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LessonCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TITLE, PREFIX_DESCRIPTION, PREFIX_DAY,
                        PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_START_TIME, PREFIX_END_TIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_TITLE, PREFIX_DAY, PREFIX_START_DATE, PREFIX_END_DATE,
                PREFIX_START_TIME, PREFIX_END_TIME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LessonCommand.MESSAGE_USAGE));
        }
        Description description = Description.defaultDescription();
        Title title = ParserUtil.parseTitle(argMultimap.getValue(PREFIX_TITLE).get());
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalTime startTime = null;
        LocalTime endTime = null;
        DayOfWeek dayOfWeek = null;

        if (argMultimap.getValue(PREFIX_START_DATE).isPresent()
                && argMultimap.getValue(PREFIX_END_DATE).isPresent()) {
            startDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_START_DATE).get());
            endDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_END_DATE).get());
        } else {
            throw new ParseException(Date.MESSAGE_CONSTRAINTS);
        }
        if (argMultimap.getValue(PREFIX_START_TIME).isPresent()
                && argMultimap.getValue(PREFIX_END_TIME).isPresent()) {
            startTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_START_TIME).get());
            endTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_END_TIME).get());
        } else {
            throw new ParseException(Time.MESSAGE_CONSTRAINTS);
        }
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
        }
        if (argMultimap.getValue(PREFIX_DAY).isPresent()) {
            dayOfWeek = ParserUtil.parseDay(argMultimap.getValue(PREFIX_DAY).get());
        } else {
            throw new ParseException(DateTime.DAY_MESSAGE_CONSTRAINTS);
        }
        requireAllNonNull(startDate, endDate, startTime, endTime, dayOfWeek);
        Lesson lesson = new Lesson(title, description, dayOfWeek, startTime, endTime, startDate, endDate);

        return new LessonCommand(lesson);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}